package ru.demon1999.sd.refactoring.servlet;

import ru.demon1999.sd.refactoring.InfoQueries.*;
import ru.demon1999.sd.refactoring.DataBase.ProductsDataBase;
import ru.demon1999.sd.refactoring.writer.WriterHTML;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        try {
            AbstractQuery q;
            if ("max".equals(command)) {
                q = new MaxQuery(writer, dataBase);
                q.printResult();
            } else if ("min".equals(command)) {
                q = new MinQuery(writer, dataBase);
                q.printResult();
            } else if ("sum".equals(command)) {
                q = new SumQuery(writer, dataBase);
                q.printResult();
            } else if ("count".equals(command)) {
                q = new CountQuery(writer, dataBase);
                q.printResult();
            } else {
                writer.printString("Unknown command: " + command);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
