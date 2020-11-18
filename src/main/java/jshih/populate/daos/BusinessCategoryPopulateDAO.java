package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.BusinessCategoryDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.BusinessCategory;

public class BusinessCategoryPopulateDAO extends JDBCDAO implements BusinessCategoryDAO {
    private static final String CREATE = "create";

    public BusinessCategoryPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Business_Category VALUES (?, ?)"));
    }

    @Override
    public void create(List<BusinessCategory> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (BusinessCategory model : models) {
                preparedStatement.setString(1, model.getBid());
                preparedStatement.setString(2, model.getCategoryName());
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
