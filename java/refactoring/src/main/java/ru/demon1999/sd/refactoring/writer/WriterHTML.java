package ru.demon1999.sd.refactoring.writer;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WriterHTML {
    private PrintWriter printWriter;
    public WriterHTML(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public void printString(String s) {
        printWriter.println(s);
    }

    public void printInt(Integer a) {
        printString(a.toString());
    }

    public void printStartTags() {
        printString("<html><body>");
    }

    public void printEndTags() {
        printString("</body></html>");
    }

    public void printNamePrice(String name, int price) {
        printString(name + "\t" + price + "</br>");
    }

    public void printWithH1(String s) {
        printString("<h1>" + s + "</h1>");
    }

    public void printNamePriceTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            String  name = rs.getString("name");
            int price  = rs.getInt("price");
            printNamePrice(name, price);
        }
    }
}
