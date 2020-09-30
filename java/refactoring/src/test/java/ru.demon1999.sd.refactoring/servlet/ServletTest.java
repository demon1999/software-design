package ru.demon1999.sd.refactoring.servlet;

import org.junit.Before;
import org.junit.Test;
import ru.demon1999.sd.refactoring.DataBase.ProductsDataBase;
import ru.demon1999.sd.refactoring.DataBase.QueryResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServletTest {

    private StringWriter stringWriter;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ProductsDataBase dataBase;
    ResultSet dummyRS;
    QueryResult dummyQR;

    @Before
    public void setUp() throws IOException, SQLException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dataBase = mock(ProductsDataBase.class);
        dummyRS = mock(ResultSet.class);
        dummyQR = mock(QueryResult.class);
        when(dummyRS.next()).thenReturn(false);
        when(dummyQR.getResultSet()).thenReturn(dummyRS);
        when(dataBase.getEveryProduct()).thenReturn(dummyQR);
        when(dataBase.getMinPricedProduct()).thenReturn(dummyQR);
        when(dataBase.getNumberOfProducts()).thenReturn(dummyQR);
        when(dataBase.getSumOfPrices()).thenReturn(dummyQR);
        when(dataBase.getMaxPricedProduct()).thenReturn(dummyQR);

        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

    }

    private void makeEmptyTable() throws SQLException {
        dataBase.dropTableIfExists();
        dataBase.createIfNotExists();
    }

    @Test
    public void testAdd() throws IOException, SQLException {
        when(request.getParameter("name")).thenReturn("iphone");
        when(request.getParameter("price")).thenReturn("365");
        new AddProductServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(), "OK\n");
    }

    @Test
    public void testGet() throws IOException, SQLException {
        new GetProductsServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                "</body></html>\n");
    }

    @Test
    public void testMin() throws IOException, SQLException {
        when(request.getParameter("command")).thenReturn("min");
        new QueryServlet(dataBase).doGet(request, response);

        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "</body></html>\n");
    }

    @Test
    public void testMax() throws IOException, SQLException {
        when(request.getParameter("command")).thenReturn("max");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "</body></html>\n");
    }

    @Test
    public void testSum() throws IOException, SQLException {
        when(dummyRS.next()).thenReturn(true);
        when(dummyRS.getInt(1)).thenReturn(930);
        when(request.getParameter("command")).thenReturn("sum");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                "Summary price: \n" +
                "930\n" +
                "</body></html>\n");
    }

    @Test
    public void testCount() throws IOException, SQLException {
        when(dummyRS.next()).thenReturn(true);
        when(dummyRS.getInt(1)).thenReturn(2);
        when(request.getParameter("command")).thenReturn("count");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                "Number of products: \n" +
                "2\n" +
                "</body></html>\n");
    }

    @Test
    public void testRealDB() throws SQLException, IOException {
        dataBase = new ProductsDataBase("jdbc:sqlite:test1.db");
        dataBase.dropTableIfExists();
        dataBase.createIfNotExists();
        for (int i = 0; i < 5; i++) {
            when(request.getParameter("name")).thenReturn("iphone" + Integer.toString(i));
            when(request.getParameter("price")).thenReturn(Integer.toString(i));
            new AddProductServlet(dataBase).doGet(request, response);
            assertEquals(stringWriter.toString(), "OK\n");
            stringWriter.getBuffer().setLength(0);
        }
        when(request.getParameter("command")).thenReturn("count");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                        "Number of products: \n" +
                        "5\n" +
                        "</body></html>\n");
        stringWriter.getBuffer().setLength(0);
        when(request.getParameter("command")).thenReturn("sum");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                "Summary price: \n" +
                "10\n" +
                "</body></html>\n");
        stringWriter.getBuffer().setLength(0);
        when(request.getParameter("command")).thenReturn("min");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "iphone0\t0</br>\n" +
                        "</body></html>\n");
        stringWriter.getBuffer().setLength(0);
        when(request.getParameter("command")).thenReturn("max");
        new QueryServlet(dataBase).doGet(request, response);
        assertEquals(stringWriter.toString(),
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "iphone4\t4</br>\n" +
                        "</body></html>\n");
        stringWriter.getBuffer().setLength(0);
    }
}
