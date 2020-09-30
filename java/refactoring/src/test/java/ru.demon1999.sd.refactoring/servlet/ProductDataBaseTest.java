package ru.demon1999.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.demon1999.sd.refactoring.DataBase.ProductsDataBase;
import ru.demon1999.sd.refactoring.DataBase.QueryResult;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductDataBaseTest {
    ProductsDataBase dataBase;

    @Before
    public void setUp() throws SQLException {
        dataBase = new ProductsDataBase("jdbc:sqlite:test1.db");
        dataBase.dropTableIfExists();
        dataBase.createIfNotExists();
        dataBase.addProductQuery("iphone", 300);
        dataBase.addProductQuery("iphone2", 565);
    }

    @Test
    public void testCount() throws SQLException {
        try (QueryResult res = dataBase.getNumberOfProducts()) {
            ResultSet rs = res.getResultSet();
            assertTrue(rs.next());
            assertEquals(rs.getInt(1), 2);
        }
    }

    @Test
    public void testSum() throws SQLException {
        QueryResult res = dataBase.getSumOfPrices();
        ResultSet rs = res.getResultSet();
        assertTrue(rs.next());
        assertEquals(rs.getInt(1), 865);
        res.close();
    }

    @Test
    public void testMax() throws SQLException {
        QueryResult res = dataBase.getMaxPricedProduct();
        ResultSet rs = res.getResultSet();
        assertTrue(rs.next());
        assertEquals(rs.getInt("price"), 565);
        res.close();
    }

    @Test
    public void testMin() throws SQLException {
        QueryResult res = dataBase.getMinPricedProduct();
        ResultSet rs = res.getResultSet();
        assertTrue(rs.next());
        assertEquals(rs.getInt("price"), 300);
        res.close();
    }

    @Test
    public void testAllProducts() throws SQLException {
        QueryResult res = dataBase.getEveryProduct();
        ResultSet rs = res.getResultSet();
        assertTrue(rs.next());
        assertEquals(rs.getInt("price"), 300);
        assertEquals(rs.getString("name"), "iphone");
        assertTrue(rs.next());
        assertEquals(rs.getInt("price"), 565);
        assertEquals(rs.getString("name"), "iphone2");
        res.close();
    }
}
