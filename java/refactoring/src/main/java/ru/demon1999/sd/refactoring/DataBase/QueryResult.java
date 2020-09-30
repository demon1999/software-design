package ru.demon1999.sd.refactoring.DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryResult implements AutoCloseable {
    private Connection c;
    private Statement stmt;
    private ResultSet rs;

    public QueryResult(Connection c, Statement stmt, ResultSet rs) {
        this.c = c;
        this.stmt = stmt;
        this.rs = rs;
    }

    public Connection getConnection() {
        return c;
    }

    public Statement getStatement() {
        return stmt;
    }

    public ResultSet getResultSet() {
        return rs;
    }

    @Override
    public void close() throws SQLException {
        rs.close();
        stmt.close();
        c.close();
    }
}
