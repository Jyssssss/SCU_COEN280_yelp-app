package jshih.application.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jshih.application.viewmodels.ReviewResult;
import jshih.model.daos.JDBCDAO;

public class ReviewQuerier extends JDBCDAO {
    public ReviewQuerier(Connection conn) {
        this.conn = conn;
    }

    public List<ReviewResult> getReviewResults(String businessUid) throws SQLException {
        if (businessUid == null)
            return List.of();

        String sql = "SELECT Stars, Review_Date, Funny_Votes, Cool_Votes, Useful_Votes, Text, Name "
                + "FROM Review R LEFT JOIN Yelp_user U ON R.User_Uid = U.User_Uid WHERE Business_Uid = ? ORDER BY Review_Date DESC";
        
        List<ReviewResult> results = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, businessUid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                results.add(new ReviewResult(resultSet.getInt("Stars"), resultSet.getDate("Review_Date"),
                        resultSet.getInt("Funny_Votes"), resultSet.getInt("Cool_Votes"),
                        resultSet.getInt("Useful_Votes"), resultSet.getString("Text"), resultSet.getString("Name")));
            }
        }

        return results;
    }
}
