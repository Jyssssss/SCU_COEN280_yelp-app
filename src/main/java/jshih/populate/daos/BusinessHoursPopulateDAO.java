package jshih.populate.daos;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.BusinessHoursDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.BusinessHours;

public class BusinessHoursPopulateDAO extends JDBCDAO implements BusinessHoursDAO {
    private static final String CREATE = "create";

    public BusinessHoursPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Business_Hours VALUES (?, ?, ?, ?)"));
    }

    @Override
    public void create(List<BusinessHours> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (BusinessHours model : models) {
                preparedStatement.setString(1, model.getBid());
                preparedStatement.setString(2, model.getDay());
                preparedStatement.setInt(3, model.getOpenTime());
                preparedStatement.setInt(4, model.getCloseTime());
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

}
