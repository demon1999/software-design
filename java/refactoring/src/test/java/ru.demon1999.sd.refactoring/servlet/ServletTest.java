package ru.demon1999.sd.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServletTest {

    private StringWriter stringWriter;
    private HttpServletResponse response;
    private HttpServletRequest request;

    @Before
    public void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);


        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    private void makeEmptyTable() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    private void addProduct(String name, Long price) throws IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(price.toString());
        new AddProductServlet().doGet(request, response);
    }

    private void initQuery() throws IOException, SQLException {
        makeEmptyTable();
        addProduct("Iphone", 365L);
        addProduct("Iphone2", 565L);
    }

    private void initSpecificQuery(String s) throws IOException, SQLException {
        initQuery();
        when(request.getParameter("command")).thenReturn(s);
        new QueryServlet().doGet(request, response);
    }
    @Test
    public void testAdd() throws IOException, SQLException {
        makeEmptyTable();
        addProduct("iphone", 365L);
        assertEquals(stringWriter.toString(), "OK\n");
    }

    @Test
    public void testGet() throws IOException, SQLException {
        initQuery();
        new GetProductsServlet().doGet(request, response);
        assertEquals(stringWriter.toString(), "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "Iphone\t365</br>\n" +
                "Iphone2\t565</br>\n" +
                "</body></html>\n");
    }

    @Test
    public void testMin() throws IOException, SQLException {
        initSpecificQuery("min");
        assertEquals(stringWriter.toString(), "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "Iphone\t365</br>\n" +
                "</body></html>\n");
    }
    @Test
    public void testMax() throws IOException, SQLException {
        initSpecificQuery("max");
        assertEquals(stringWriter.toString(), "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "Iphone2\t565</br>\n" +
                "</body></html>\n");
    }

    @Test
    public void testSum() throws IOException, SQLException {
        initSpecificQuery("sum");
        assertEquals(stringWriter.toString(), "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "Summary price: \n" +
                "930\n" +
                "</body></html>\n");
    }

    @Test
    public void testCount() throws IOException, SQLException {
        initSpecificQuery("count");
        assertEquals(stringWriter.toString(), "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "Number of products: \n" +
                "2\n" +
                "</body></html>\n");
    }
}
