package jshih.populate.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jshih.model.daos.JDBCDAO;
import jshih.model.daos.YelpUserDAO;
import jshih.model.models.YelpUser;

public class YelpUserPopulateDAO extends JDBCDAO implements YelpUserDAO {
    private static final String CREATE = "create";

    public YelpUserPopulateDAO(Connection conn) throws SQLException {
        this.conn = conn;
        statements = new HashMap<>();
        statements.put(CREATE, conn.prepareStatement("INSERT INTO Yelp_User VALUES (?, ?, ?)"));
    }

    @Override
    public void create(List<YelpUser> models) {
        PreparedStatement preparedStatement = (PreparedStatement) statements.get(CREATE);
        try {
            for (YelpUser model : models) {
                preparedStatement.setString(1, model.getUid());
                preparedStatement.setString(2, model.getUserUid());
                preparedStatement.setString(3, model.getName());
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
    public YelpUser getYelpUser(String userUid) {
        throw new UnsupportedOperationException();
    }

}
