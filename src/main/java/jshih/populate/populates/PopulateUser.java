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

import jshih.model.AppConfiguration;
import jshih.model.daos.YelpUserDAO;
import jshih.model.models.YelpUser;
import jshih.populate.daos.YelpUserPopulateDAO;

public class PopulateUser {
    private static final String USER_ID_FORMAT = "U%09d";

    private PopulateUser() {
    }

    public static void populate(Connection conn, String file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        YelpUserDAO yelpUserDAO = new YelpUserPopulateDAO(conn);

        List<YelpUser> userBuffer = new ArrayList<>();

        delete(conn);

        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            final AtomicInteger seq = new AtomicInteger();
            stream.forEach(line -> {
                try {
                    YelpUser user = objectMapper.readValue(line, YelpUser.class);
                    user.setUid(String.format(USER_ID_FORMAT, seq.incrementAndGet()));
                    userBuffer.add(user);

                    if (userBuffer.size() > AppConfiguration.BATCH_CAPACITY) {
                        yelpUserDAO.create(userBuffer);
                        userBuffer.clear();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }
        if (!userBuffer.isEmpty())
            yelpUserDAO.create(userBuffer);
    }

    private static void delete(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM Yelp_User");
        } catch (SQLException e) {
            while (e != null) {
                e.printStackTrace();
                e = e.getNextException();
            }
        }
    }
}
