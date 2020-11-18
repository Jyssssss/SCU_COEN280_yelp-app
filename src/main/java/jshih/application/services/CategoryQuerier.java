package jshih.application.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import jshih.application.enums.SearchFor;
import jshih.model.daos.JDBCDAO;

public class CategoryQuerier extends JDBCDAO {
    private static final String STATEMENT = "statement";

    public CategoryQuerier(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(STATEMENT, conn.createStatement());
    }

    public List<String> getMainCategoryNames() throws SQLException {
        ResultSet resultSet = statements.get(STATEMENT)
                .executeQuery("SELECT Name FROM CATEGORY WHERE Is_Main = 1 ORDER BY Name ASC");
        List<String> names = new ArrayList<>();
        while (resultSet.next()) {
            names.add(resultSet.getString("Name"));
        }
        return names;
    }

    public List<String> getSubCategoryNames(List<String> mainNames, SearchFor searchFor) throws SQLException {
        if (mainNames == null || mainNames.isEmpty())
            return List.of();
        List<String> params = mainNames.stream().map(n -> "?").collect(Collectors.toList());
        String sql = "SELECT DISTINCT(SC.Name) FROM Category_Relation R JOIN Category SC ON R.Sub_Cid = SC.Cid JOIN Category MC ON R.Main_Cid = MC.Cid GROUP BY SC.Name HAVING SUM(CASE WHEN MC.Name IN ("
                + String.join(", ", params) + ") THEN 1 END)"
                + (searchFor == SearchFor.AND ? (" = " + Integer.toString(mainNames.size())) : " > 0")
                + " ORDER BY SC.Name ASC";

        List<String> names = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (int i = 0; i < mainNames.size(); i++) {
                statement.setString(i + 1, mainNames.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString("Name"));
            }
        }

        return names;
    }
}
