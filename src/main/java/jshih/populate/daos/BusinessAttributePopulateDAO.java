package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.BusinessAttributeDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.BusinessAttribute;

public class BusinessAttributePopulateDAO extends JDBCDAO implements BusinessAttributeDAO {
    private static final String CREATE = "create";

    public BusinessAttributePopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Business_Attribute VALUES (?, ?)"));
    }

    @Override
    public void create(List<BusinessAttribute> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (BusinessAttribute model : models) {
                preparedStatement.setString(1, model.getBid());
                preparedStatement.setString(2, model.getName());
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
