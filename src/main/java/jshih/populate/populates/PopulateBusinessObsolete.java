package jshih.populate.populates;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jshih.model.AppConfiguration;
import jshih.model.daos.*;
import jshih.model.helpers.DayTimeHelper;
import jshih.model.models.*;
import jshih.populate.daos.*;

public class PopulateBusinessObsolete {
    private static final String BUSINESS_ID_FORMAT = "B%09d";
    private static final String CATEGORY_ID_FORMAT = "C%09d";

    private PopulateBusinessObsolete() {
    }

    public static void populate(Connection conn, String file) throws Exception {
        JSONParser parser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Set<String> mainCategories = Set.copyOf(AppConfiguration.getMainCategories());
        Map<String, String> mainCategoryMap = new HashMap<>();
        Map<String, String> subCategoryMap = new HashMap<>();
        Map<String, Map<String, Integer>> categoryRelationMap = new HashMap<>();

        CategoryDAO categoryDAO = new CategoryPopulateDAO(conn);
        CategoryRelationDAO categoryRelationDAO = new CategoryRelationPopulateDAO(conn);
        BusinessDAO businessDAO = new BusinessPopulateDAO(conn);
        BusinessHoursDAO businessHoursDAO = new BusinessHoursPopulateDAO(conn);
        BusinessCategoryDAO businessCategoryDAO = new BusinessCategoryPopulateDAO(conn);
        BusinessAttributeDAO businessAttributeDAO = new BusinessAttributePopulateDAO(conn);

        List<Category> categoryBuffer = new ArrayList<>();
        List<Business> businessBuffer = new ArrayList<>();
        List<BusinessHours> businessHoursBuffer = new ArrayList<>();
        List<BusinessCategory> businessCategoryBuffer = new ArrayList<>();
        List<BusinessAttribute> businessAttributeBuffer = new ArrayList<>();

        // Delete all rows in tables
        delete(conn);

        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            final AtomicInteger businessSeq = new AtomicInteger();
            final AtomicInteger categorySeq = new AtomicInteger();
            stream.forEach(line -> {
                try {
                    // Insert Business
                    Business business = objectMapper.readValue(line, Business.class);
                    business.setBid(String.format(BUSINESS_ID_FORMAT, businessSeq.incrementAndGet()));
                    businessBuffer.add(business);

                    // Insert BusinessHours
                    JSONObject rawObject = (JSONObject) parser.parse(line);
                    JSONObject dayHours = (JSONObject) rawObject.get("hours");
                    businessHoursBuffer
                            .addAll(DayTimeHelper.DAYS.stream().map(d -> parseDay(dayHours, business.getBid(), d))
                                    .flatMap(Collection::stream).collect(Collectors.toList()));

                    // Category and BusinessCategory
                    JSONArray jsonCatetories = ((JSONArray) rawObject.get("categories"));
                    List<String> categories = IntStream.range(0, jsonCatetories.size()).mapToObj(jsonCatetories::get)
                            .map(Object::toString).collect(Collectors.toList());

                    var curMainCategories = categories.stream().filter(mainCategories::contains)
                            .collect(Collectors.toList());

                    // Insert Main-Categories
                    curMainCategories.forEach(m -> {
                        Category category = createCategory(m, mainCategoryMap, true, categorySeq);
                        if (category != null)
                            categoryBuffer.add(category);
                    });

                    // Insert Sub-Categories
                    categories.stream().filter(c -> !mainCategories.contains(c)).forEach(c -> {
                        Category category = createCategory(c, subCategoryMap, false, categorySeq);
                        if (category != null)
                            categoryBuffer.add(category);

                        categoryRelationMap.computeIfAbsent(c, k -> new HashMap<>());
                        curMainCategories.forEach(m -> categoryRelationMap.get(c).put(m,
                                categoryRelationMap.get(c).getOrDefault(m, 0) + 1));
                    });

                    // Insert BusinessCategory
                    businessCategoryBuffer.addAll(categories.stream()
                            .map(c -> new BusinessCategory(business.getBid(), c)).collect(Collectors.toList()));

                    // Insert BusinessAttributes
                    businessAttributeBuffer.addAll(JsonFlattener
                            .flattenAsMap(((JSONObject) rawObject.get("attributes")).toJSONString()).entrySet().stream()
                            .map(entry -> new BusinessAttribute(business.getBid(),
                                    entry.getKey().replace('.', '_') + "_" + entry.getValue().toString()))
                            .collect(Collectors.toList()));

                    if (businessBuffer.size() > AppConfiguration.BATCH_CAPACITY) {
                        categoryDAO.create(categoryBuffer);
                        businessDAO.create(businessBuffer);
                        businessHoursDAO.create(businessHoursBuffer);
                        businessCategoryDAO.create(businessCategoryBuffer);
                        businessAttributeDAO.create(businessAttributeBuffer);

                        categoryBuffer.clear();
                        businessBuffer.clear();
                        businessHoursBuffer.clear();
                        businessCategoryBuffer.clear();
                        businessAttributeBuffer.clear();
                    }

                } catch (ParseException | JsonProcessingException e) {
                    e.printStackTrace();
                }
            });

            if (!businessBuffer.isEmpty()) {
                categoryDAO.create(categoryBuffer);
                businessDAO.create(businessBuffer);
                businessHoursDAO.create(businessHoursBuffer);
                businessCategoryDAO.create(businessCategoryBuffer);
                businessAttributeDAO.create(businessAttributeBuffer);
            }

            // Insert Category Relations
            List<CategoryRelation> relationBuffer = new ArrayList<>();
            categoryRelationMap.forEach((k, v) -> {
                int max = Collections.max(v.values());
                v.entrySet().stream().filter(entry -> entry.getValue() == max).forEach(entry -> relationBuffer
                        .add(new CategoryRelation(mainCategoryMap.get(entry.getKey()), subCategoryMap.get(k))));

                if (relationBuffer.size() > AppConfiguration.BATCH_CAPACITY) {
                    categoryRelationDAO.create(relationBuffer);
                    relationBuffer.clear();
                }
            });

            if (!relationBuffer.isEmpty())
                categoryRelationDAO.create(relationBuffer);

        }
    }

    private static void delete(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            statement.addBatch("DELETE FROM Business_Hours");
            statement.addBatch("DELETE FROM Business_Attribute");
            statement.addBatch("DELETE FROM Business_Category");
            statement.addBatch("DELETE FROM Business");
            statement.addBatch("DELETE FROM Category_Relation");
            statement.addBatch("DELETE FROM Category");
            statement.executeBatch();
        } catch (SQLException e) {
            while (e != null) {
                e.printStackTrace();
                e = e.getNextException();
            }
        }
    }

    private static List<BusinessHours> parseDay(JSONObject dayHours, String bid, String day) {
        JSONObject hours = (JSONObject) dayHours.get(day);
        if (hours != null) {
            Time openTime = DayTimeHelper.getOpenTime((String) hours.get("open"));
            Time closeTime = DayTimeHelper.getCloseTime((String) hours.get("close"));
            return openTime.getTime() < closeTime.getTime() ? List.of(new BusinessHours(bid, day, openTime, closeTime))
                    : List.of(new BusinessHours(bid, day, openTime, DayTimeHelper.getEndMidnightTime()),
                            new BusinessHours(bid, DayTimeHelper.getNextDay(day), DayTimeHelper.getStartMidnightTime(),
                                    closeTime));
        }
        return List.of();
    }

    private static Category createCategory(String name, Map<String, String> map, boolean isMain, AtomicInteger seq) {
        if (!map.containsKey(name)) {
            Category category = new Category(String.format(CATEGORY_ID_FORMAT, seq.incrementAndGet()), name, isMain);
            map.put(name, category.getCid());
            return category;
        }
        return null;
    }
}
