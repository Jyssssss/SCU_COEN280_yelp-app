package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.CategoryRelationDAO;
import jshih.model.daos.JDBCDAO;
import jshih.model.models.CategoryRelation;

public class CategoryRelationPopulateDAO extends JDBCDAO implements CategoryRelationDAO {
    private static final String CREATE = "create";

    public CategoryRelationPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Category_Relation VALUES (?, ?)"));
    }

    @Override
    public void create(List<CategoryRelation> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (CategoryRelation model : models) {
                preparedStatement.setString(1, model.getMainCid());
                preparedStatement.setString(2, model.getSubCid());
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
