package jshih.populate.daos;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.BusinessDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.Business;

public class BusinessPopulateDAO extends JDBCDAO implements BusinessDAO {
    private static final String CREATE = "create";

    public BusinessPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Business VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"));
    }

    @Override
    public void create(List<Business> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (Business model : models) {
                preparedStatement.setString(1, model.getBid());
                preparedStatement.setString(2, model.getBusinessUid());
                preparedStatement.setString(3, model.getName());
                preparedStatement.setString(4, model.getAddress());
                preparedStatement.setString(5, model.getCity());
                preparedStatement.setString(6, model.getState());
                preparedStatement.setFloat(7, model.getStars());
                preparedStatement.setInt(8, model.getReviewCount());
                preparedStatement.setBoolean(9, model.getIsOpen());
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
    public Business getBusiness(String buisnessUid) {
        throw new UnsupportedOperationException();
    }
}
