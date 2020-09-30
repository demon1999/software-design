package ru.demon1999.sd.refactoring.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductsDataBase {
    private final String path;

    private void updateQuery(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection(path)) { ;
            Statement stmt = c.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }
    }

    public void createIfNotExists() throws SQLException {
        String createQuery = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        updateQuery(createQuery);
    }

    public ProductsDataBase(String path) throws SQLException {
        this.path = path;
        createIfNotExists();
    }

    public void dropTableIfExists() throws SQLException {
        updateQuery("DROP TABLE IF EXISTS PRODUCT");
    }


    public void addProductQuery(String name, long price) throws SQLException {
        updateQuery("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\""
                + name + "\"," + price + ")");
    }


}
