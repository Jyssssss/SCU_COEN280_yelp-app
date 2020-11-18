package jshih.model.daos;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public abstract class JDBCDAO {
    protected Connection conn;
    protected Map<String, Statement> statements;
}
