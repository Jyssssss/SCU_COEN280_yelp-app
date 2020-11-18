package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.JDBCDAO;
import jshih.model.daos.ReviewDAO;
import jshih.model.models.Review;

public class ReviewPopulateDAO extends JDBCDAO implements ReviewDAO {
    private static final String CREATE = "create";

    public ReviewPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Review VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"));
    }

    @Override
    public void create(List<Review> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (Review model : models) {
                preparedStatement.setString(1, model.getRid());
                preparedStatement.setString(2, model.getReviewUid());
                preparedStatement.setString(3, model.getUserUid());
                preparedStatement.setString(4, model.getBusinessUid());
                preparedStatement.setInt(5, model.getStars());
                preparedStatement.setDate(6, new Date(model.getReviewDate().getTime()));
                preparedStatement.setInt(7, model.getFunnyVotes());
                preparedStatement.setInt(8, model.getCoolVotes());
                preparedStatement.setInt(9, model.getUsefulVotes());
                preparedStatement.setString(10, model.getText());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            while (e != null) {
                e.printStackTrace();
                e = e.getNextException();
            }
        }
    }

    @Override
    public Review getReview(String reviewUid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Review> getUserReviews(String userUid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Review> getBusinessReviews(String businessUid) {
        throw new UnsupportedOperationException();
    }

}
