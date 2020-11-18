package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.CheckinDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.Checkin;

public class CheckinPopulateDAO extends JDBCDAO implements CheckinDAO {
    private static final String CREATE = "create";

    public CheckinPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Checkin VALUES (?, ?, ?, ?, ?)"));
    }

    @Override
    public void create(List<Checkin> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (Checkin model : models) {
                preparedStatement.setString(1, model.getCid());
                preparedStatement.setString(2, model.getBusinessUid());
                preparedStatement.setInt(3, model.getDay());
                preparedStatement.setInt(4, model.getHour());
                preparedStatement.setInt(5, model.getCount());
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
