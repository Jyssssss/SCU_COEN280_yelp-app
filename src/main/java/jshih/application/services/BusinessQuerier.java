package jshih.application.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jshih.application.enums.SearchFor;
import jshih.application.viewmodels.BusinessDaysTimes;
import jshih.application.viewmodels.BusinessResult;
import jshih.model.models.Time;
import jshih.model.daos.JDBCDAO;
import jshih.model.helpers.DayTimeHelper;

public class BusinessQuerier extends JDBCDAO {
    public BusinessQuerier(Connection conn) {
        this.conn = conn;
    }

    public List<String> getAttributeNames(List<String> mainCategories, List<String> subCategories,
            SearchFor mainSearchFor, SearchFor subSearchFor) throws SQLException {
        mainCategories = mainCategories != null ? mainCategories : List.of();
        subCategories = subCategories != null ? subCategories : List.of();

        if (mainCategories.isEmpty())
            return List.of();

        String innerSql = buildInnerSQL(mainCategories, subCategories, null, null, mainSearchFor, subSearchFor, null);
        String sql = "SELECT DISTINCT(Name) FROM Business_Attribute WHERE Bid IN (" + innerSql + ") ORDER BY Name ASC";

        List<String> names = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            List<String> params = Stream.of(mainCategories, subCategories).flatMap(Collection::stream)
                    .collect(Collectors.toList());
            for (int i = 0; i < params.size(); i++) {
                statement.setString(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString("Name"));
            }
        }

        return names;
    }

    public List<String> getLocations(List<String> mainCategories, List<String> subCategories, List<String> attributes,
            SearchFor mainSearchFor, SearchFor subSearchFor, SearchFor attrSearchFor) throws SQLException {
        mainCategories = mainCategories != null ? mainCategories : List.of();
        subCategories = subCategories != null ? subCategories : List.of();
        attributes = attributes != null ? attributes : List.of();

        if (mainCategories.isEmpty())
            return List.of();

        String innerSql = buildInnerSQL(mainCategories, subCategories, attributes, null, mainSearchFor, subSearchFor,
                attrSearchFor);

        String sql = "SELECT DISTINCT(CONCAT(City, CONCAT(', ', State))) AS Place FROM Business WHERE Bid IN ("
                + innerSql + ") ORDER BY Place";

        List<String> names = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            List<String> params = Stream.of(mainCategories, subCategories, attributes).flatMap(Collection::stream)
                    .collect(Collectors.toList());
            for (int i = 0; i < params.size(); i++) {
                statement.setString(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString("Place"));
            }
        }

        return names;
    }

    public BusinessDaysTimes getDaysTimes(List<String> mainCategories, List<String> subCategories,
            List<String> attributes, String location, SearchFor mainSearchFor, SearchFor subSearchFor,
            SearchFor attrSearchFor) throws SQLException {
        mainCategories = mainCategories != null ? mainCategories : List.of();
        subCategories = subCategories != null ? subCategories : List.of();
        attributes = attributes != null ? attributes : List.of();

        if (mainCategories.isEmpty())
            return null;

        String innerSql = buildInnerSQL(mainCategories, subCategories, attributes, location, mainSearchFor,
                subSearchFor, attrSearchFor);

        String sql = "SELECT Day, Open_Time, Close_Time FROM Business_Hours WHERE Bid IN (" + innerSql + ") ORDER BY "
                + "CASE WHEN Day = 'Sunday' THEN 1 " + "WHEN Day = 'Monday' THEN 2 " + "WHEN Day = 'Tuesday' THEN 3 "
                + "WHEN Day = 'Wednesday' THEN 4 " + "WHEN Day = 'Thursday' THEN 5 " + "WHEN Day = 'Friday' THEN 6 "
                + "WHEN Day = 'Saturday' THEN 7 " + "END ASC";

        BusinessDaysTimes businessDaysTimes = null;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            List<String> locations = List.of();
            if (location != null) {
                String[] splits = location.split(",");
                locations = List.of(splits[0].trim(), splits[1].trim());
            }
            List<String> params = Stream.of(mainCategories, subCategories, attributes, locations)
                    .flatMap(Collection::stream).collect(Collectors.toList());
            for (int i = 0; i < params.size(); i++) {
                statement.setString(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            businessDaysTimes = new BusinessDaysTimes();
            Set<String> days = new LinkedHashSet<>();
            Set<Integer> froms = new HashSet<>();
            Set<Integer> tos = new HashSet<>();
            while (resultSet.next()) {
                days.add(resultSet.getString("Day"));
                int openTime = resultSet.getInt("Open_Time");
                int closeTime = DayTimeHelper.getCloseTime(resultSet.getInt("Close_Time"));
                froms.add(openTime);
                tos.add(closeTime);
            }
            businessDaysTimes.setDays(days.stream().collect(Collectors.toList()));
            businessDaysTimes.setFroms(froms.stream().sorted().map(Time::new).collect(Collectors.toList()));
            businessDaysTimes.setTos(tos.stream().sorted().map(Time::new).collect(Collectors.toList()));
        }

        return businessDaysTimes;
    }

    public List<BusinessResult> getBusinessResults(List<String> mainCategories, List<String> subCategories,
            List<String> attributes, String location, String day, Integer from, Integer to, SearchFor mainSearchFor,
            SearchFor subSearchFor, SearchFor attrSearchFor) throws SQLException {
        mainCategories = mainCategories != null ? mainCategories : List.of();
        subCategories = subCategories != null ? subCategories : List.of();
        attributes = attributes != null ? attributes : List.of();

        if (mainCategories.isEmpty())
            return List.of();

        String innerSql = buildInnerSQL(mainCategories, subCategories, attributes, location, mainSearchFor,
                subSearchFor, attrSearchFor);

        String sql = buildBusinessSQL(innerSql, day, from, to);

        List<BusinessResult> results = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            List<String> locations = List.of();
            if (location != null) {
                String[] splits = location.split(",");
                locations = List.of(splits[0].trim(), splits[1].trim());
            }
            List<String> params = Stream.of(mainCategories, subCategories, attributes, locations)
                    .flatMap(Collection::stream).collect(Collectors.toList());
            int i = 0;
            for (String param : params) {
                statement.setString(++i, param);
            }

            if (day != null)
                statement.setString(++i, day);

            if (from != null) {
                statement.setInt(++i, from);
            }
            if (to != null) {
                statement.setInt(++i, to);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                results.add(new BusinessResult(resultSet.getString("BusinessUid"), resultSet.getString("Name"),
                        resultSet.getString("Address").replace("\n", " "), resultSet.getString("City"),
                        resultSet.getString("State"), resultSet.getFloat("Stars"), resultSet.getInt("Reviews"),
                        resultSet.getInt("Checkins")));
            }
        }

        return results;
    }

    private String buildInnerSQL(List<String> mainCategories, List<String> subCategories, List<String> attributes,
            String location, SearchFor mainSearchFor, SearchFor subSearchFor, SearchFor attrSearchFor) {
        List<String> mainParams = mainCategories.stream().map(n -> "?").collect(Collectors.toList());
        String innerSql = "SELECT B1.Bid FROM Business B1 RIGHT JOIN Business_Category BC1 ON B1.Bid = BC1.Bid GROUP BY B1.Bid HAVING SUM(CASE WHEN Category_Name IN ("
                + String.join(", ", mainParams) + ") THEN 1 END)"
                + (mainSearchFor == SearchFor.AND ? (" = " + Integer.toString(mainCategories.size())) : " > 0");

        if (subCategories != null && !subCategories.isEmpty()) {
            List<String> subParams = subCategories.stream().map(n -> "?").collect(Collectors.toList());
            innerSql += " INTERSECT SELECT B2.Bid FROM Business B2 RIGHT JOIN Business_Category BC2 ON B2.Bid = BC2.Bid GROUP BY B2.Bid HAVING SUM(CASE WHEN Category_Name IN ("
                    + String.join(", ", subParams) + ") THEN 1 END)"
                    + (subSearchFor == SearchFor.AND ? (" = " + Integer.toString(subCategories.size())) : " > 0");
        }

        if (attributes != null && !attributes.isEmpty()) {
            List<String> attrParams = attributes.stream().map(n -> "?").collect(Collectors.toList());
            innerSql += " INTERSECT SELECT B3.Bid FROM Business B3 RIGHT JOIN Business_Attribute BA ON B3.Bid = BA.Bid GROUP BY B3.Bid HAVING SUM(CASE WHEN BA.Name IN ("
                    + String.join(", ", attrParams) + ") THEN 1 END) "
                    + (attrSearchFor == SearchFor.AND ? (" = " + Integer.toString(attributes.size())) : " > 0");
        }

        if (location != null)
            innerSql += " INTERSECT SELECT B4.Bid FROM Business B4 WHERE City = ? AND State = ?";

        return innerSql;
    }

    private String buildBusinessSQL(String innerSql, String day, Integer from, Integer to) {
        if (day != null || from != null || to != null) {
            innerSql += " INTERSECT SELECT Bid FROM Business_Hours WHERE 1 = 1";
            if (day != null)
                innerSql += " AND Day = ?";
            if (from != null)
                innerSql += " AND ? < Close_Time";
            if (to != null)
                innerSql += " AND Open_Time < ?";
        }

        return "SELECT B.Business_Uid AS BusinessUid, Name, Address, City, State, Stars, Reviews, Checkins "
                + "FROM ( SELECT BR.Business_Uid, Reviews, SUM(Count) Checkins "
                + "FROM ( SELECT B5.Business_Uid, COUNT(*) Reviews "
                + "FROM ( SELECT Business_Uid FROM Business WHERE Bid IN (" + innerSql
                + ") ) B5 LEFT JOIN Review R ON B5.Business_Uid = R.Business_Uid GROUP BY B5.Business_Uid ) BR "
                + "LEFT JOIN Checkin C ON BR.Business_Uid = C.Business_Uid GROUP BY BR.Business_Uid, Reviews ) BRC "
                + "LEFT JOIN Business B ON BRC.Business_Uid = B.Business_Uid ORDER BY Name";
    }
}
