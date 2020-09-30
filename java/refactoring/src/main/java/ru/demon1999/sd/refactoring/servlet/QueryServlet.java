package ru.demon1999.sd.refactoring.servlet;

import ru.demon1999.sd.refactoring.DataBase.ProductsDataBase;
import ru.demon1999.sd.refactoring.DataBase.QueryResult;
import ru.demon1999.sd.refactoring.writer.WriterHTML;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private ProductsDataBase dataBase;
    public QueryServlet(ProductsDataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        WriterHTML writer = new WriterHTML(response.getWriter());

        if ("max".equals(command)) {
            try {
                QueryResult res = dataBase.getMaxPricedProduct();
                ResultSet rs = res.getResultSet();
                writer.printStartTags();
                writer.printWithH1("Product with max price: ");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    writer.printNamePrice(name, price);
                }

                writer.printEndTags();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                QueryResult res = dataBase.getMinPricedProduct();
                ResultSet rs = res.getResultSet();
                writer.printStartTags();
                writer.printWithH1("Product with min price: ");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    writer.printNamePrice(name, price);
                }

                writer.printEndTags();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                QueryResult res = dataBase.getSumOfPrices();
                ResultSet rs = res.getResultSet();
                writer.printStartTags();
                writer.printString("Summary price: ");

                if (rs.next()) {
                    writer.printInt(rs.getInt(1));
                }
                writer.printEndTags();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                QueryResult res = dataBase.getNumberOfProducts();
                ResultSet rs = res.getResultSet();
                writer.printStartTags();
                writer.printString("Number of products: ");

                if (rs.next()) {
                    writer.printInt(rs.getInt(1));
                }
                writer.printEndTags();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            writer.printString("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
