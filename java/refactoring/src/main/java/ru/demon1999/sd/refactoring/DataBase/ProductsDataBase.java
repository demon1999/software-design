package ru.demon1999.sd.refactoring.DataBase;

import java.sql.*;

public class ProductsDataBase {
    private final String path;

    private void updateQuery(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection(path)) { ;
            Statement stmt = c.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }
    }

    private QueryResult infoQuery(String query) throws SQLException {
        Connection c = DriverManager.getConnection(path);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return new QueryResult(c, stmt, rs);
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

    public QueryResult getEveryProduct() throws SQLException {
        return infoQuery("SELECT * FROM PRODUCT");
    }

    public QueryResult getMinPricedProduct() throws SQLException {
        return infoQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
    }

    public QueryResult getMaxPricedProduct() throws SQLException {
        return infoQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    public QueryResult getNumberOfProducts() throws  SQLException {
        return infoQuery("SELECT COUNT(*) FROM PRODUCT");
    }

    public QueryResult getSumOfPrices() throws SQLException {
        return infoQuery("SELECT SUM(price) FROM PRODUCT");
    }
}
