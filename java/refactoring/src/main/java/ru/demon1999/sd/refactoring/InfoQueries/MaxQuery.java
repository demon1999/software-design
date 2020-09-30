package ru.demon1999.sd.refactoring.InfoQueries;

import ru.demon1999.sd.refactoring.DataBase.ProductsDataBase;
import ru.demon1999.sd.refactoring.DataBase.QueryResult;
import ru.demon1999.sd.refactoring.writer.WriterHTML;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaxQuery extends AbstractQuery {
    private WriterHTML writer;
    private QueryResult res;

    public MaxQuery(WriterHTML writer, ProductsDataBase dataBase) throws SQLException {
        this.writer = writer;
        this.res = dataBase.getMinPricedProduct();
    }

    @Override
    QueryResult result() {
        return res;
    }

    @Override
    WriterHTML getWriter() {
        return writer;
    }

    @Override
    void printResultBody() throws SQLException {
        ResultSet rs = res.getResultSet();
        writer.printWithH1("Product with max price: ");
        writer.printNamePriceTable(rs);
    }
}
