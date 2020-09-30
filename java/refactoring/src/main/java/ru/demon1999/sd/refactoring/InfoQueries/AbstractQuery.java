package ru.demon1999.sd.refactoring.InfoQueries;

import ru.demon1999.sd.refactoring.DataBase.QueryResult;
import ru.demon1999.sd.refactoring.writer.WriterHTML;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class AbstractQuery {


    abstract QueryResult result();
    abstract WriterHTML getWriter();
    abstract void printResultBody() throws SQLException;

    public void printResult() {
        try {
            QueryResult res = result();
            ResultSet rs = res.getResultSet();
            WriterHTML writer = getWriter();
            writer.printStartTags();
            printResultBody();
            writer.printEndTags();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
