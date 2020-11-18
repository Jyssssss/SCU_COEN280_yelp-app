package jshih.populate.populates;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jshih.model.AppConfiguration;
import jshih.model.daos.ReviewDAO;
import jshih.model.models.Review;
import jshih.populate.daos.ReviewPopulateDAO;

public class PopulateReview {
    private static final String REVIEW_ID_FORMAT = "R%09d";

    private PopulateReview() {
    }

    public static void populate(Connection conn, String file) throws Exception {
        JSONParser parser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ReviewDAO reviewDAO = new ReviewPopulateDAO(conn);

        List<Review> reviewBuffer = new ArrayList<>();

        delete(conn);

        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            final AtomicInteger seq = new AtomicInteger();
            stream.forEach(line -> {
                try {
                    Review review = objectMapper.readValue(line, Review.class);
                    review.setRid(String.format(REVIEW_ID_FORMAT, seq.incrementAndGet()));
                    JSONObject rawObject = (JSONObject) parser.parse(line);
                    JSONObject votes = (JSONObject) rawObject.get("votes");
                    review.setFunnyVotes(((Long) votes.get("funny")).intValue());
                    review.setCoolVotes(((Long) votes.get("cool")).intValue());
                    review.setUsefulVotes(((Long) votes.get("useful")).intValue());

                    reviewBuffer.add(review);

                    if (reviewBuffer.size() > AppConfiguration.BATCH_CAPACITY) {
                        reviewDAO.create(reviewBuffer);
                        reviewBuffer.clear();
                    }
                } catch (JsonProcessingException | ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        if (!reviewBuffer.isEmpty())
            reviewDAO.create(reviewBuffer);
    }

    private static void delete(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM Review");
        } catch (SQLException e) {
            while (e != null) {
                e.printStackTrace();
                e = e.getNextException();
            }
        }
    }
}
