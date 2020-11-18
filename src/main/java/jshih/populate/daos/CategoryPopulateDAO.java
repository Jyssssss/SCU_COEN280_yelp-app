package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.CategoryDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.Category;

public class CategoryPopulateDAO extends JDBCDAO implements CategoryDAO {
    private static final String CREATE = "create";

    public CategoryPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Category VALUES (?, ?, ?)"));
    }

    @Override
    public void create(List<Category> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (Category model : models) {
                preparedStatement.setString(1, model.getCid());
                preparedStatement.setString(2, model.getName());
                preparedStatement.setBoolean(3, model.getIsMain());
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
    public Category getCategory(String name) {
        throw new UnsupportedOperationException();
    }

}
