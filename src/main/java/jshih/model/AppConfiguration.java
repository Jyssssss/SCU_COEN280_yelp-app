package jshih.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AppConfiguration {
    public static final String DRIVER_NAME;
    public static final String DB_NAME;
    public static final String DB_USERNAME;
    public static final String DB_PASSWORD;
    public static final int BATCH_CAPACITY;
    private static final List<String> MAIN_CATEGORIES;

    private AppConfiguration() {
    }

    static {
        String driver = null;
        String url = null;
        String username = null;
        String password = null;
        int batchcapacity = 1;
        List<String> categories = null;
        try (InputStream stream = AppConfiguration.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(stream);
            driver = props.getProperty("datasource.driver");
            url = props.getProperty("datasource.url");
            username = props.getProperty("datasource.username");
            password = props.getProperty("datasource.password");
            batchcapacity = Integer.parseInt(props.getProperty("data.batchcapacity"));
            categories = List.of(Arrays.stream(props.getProperty("data.categories").split(",")).map(String::trim)
                    .toArray(String[]::new));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DRIVER_NAME = driver;
            DB_NAME = url;
            DB_USERNAME = username;
            DB_PASSWORD = password;
            BATCH_CAPACITY = batchcapacity;
            MAIN_CATEGORIES = categories;
        }
    }

    public static List<String> getMainCategories() {
        return MAIN_CATEGORIES;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        return DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);
    }
}
