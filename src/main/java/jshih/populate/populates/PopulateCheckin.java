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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jshih.model.AppConfiguration;
import jshih.model.daos.CheckinDAO;
import jshih.model.models.Checkin;
import jshih.populate.daos.CheckinPopulateDAO;

public class PopulateCheckin {
    private static final String CHECKIN_ID_FORMAT = "C%09d";

    private PopulateCheckin() {
    }

    public static void populate(Connection conn, String file) throws Exception {
        JSONParser parser = new JSONParser();

        CheckinDAO checkinDAO = new CheckinPopulateDAO(conn);

        List<Checkin> checkinBuffer = new ArrayList<>();

        delete(conn);

        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            final AtomicInteger seq = new AtomicInteger();
            stream.forEach(line -> {
                try {
                    JSONObject rawObject = (JSONObject) parser.parse(line);
                    String uid = (String) rawObject.get("business_id");
                    ((JSONObject) rawObject.get("checkin_info")).forEach((k, v) -> {
                        var time = ((String) k).split("-");
                        checkinBuffer.add(new Checkin(String.format(CHECKIN_ID_FORMAT, seq.incrementAndGet()), uid,
                                Integer.parseInt(time[1]), Integer.parseInt(time[0]), ((Long) v).intValue()));
                    });

                    if (checkinBuffer.size() > AppConfiguration.BATCH_CAPACITY) {
                        checkinDAO.create(checkinBuffer);
                        checkinBuffer.clear();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        if (!checkinBuffer.isEmpty())
            checkinDAO.create(checkinBuffer);
    }

    private static void delete(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM Checkin");
        } catch (SQLException e) {
            while (e != null) {
                e.printStackTrace();
                e = e.getNextException();
            }
        }
    }
}
