package demon1999.dao;

import demon1999.model.CompanyStocks;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDao implements Dao {
    private List<CompanyStocks> companies = new ArrayList<>();

    boolean checkId(int companyId) {
        return companyId >= 0 && companyId < companies.size();
    }

    @Override
    public int addCompany(String name) {
        int id = companies.size();
        companies.add(new CompanyStocks(name));
        return id;
    }

    @Override
    public boolean changePrice(int companyId, double newPrice) {
        if (!checkId(companyId) && newPrice > 0.0)
            return false;
        CompanyStocks stocks = companies.get(companyId);
        companies.set(companyId,
                new CompanyStocks(stocks.getCompanyName(), newPrice, stocks.getQuantity()));
        return true;
    }

    @Override
    public double getPrice(int companyId) {
        if (!checkId(companyId))
            return -1.0;
        CompanyStocks stocks = companies.get(companyId);
        return stocks.getPrice();
    }

    @Override
    public int getQuantity(int companyId) {
        if (!checkId(companyId))
            return -1;
        CompanyStocks stocks = companies.get(companyId);
        return stocks.getQuantity();
    }

    @Override
    public boolean buyStocks(int companyId, int quantity) {
        if (!checkId(companyId))
            return false;
        CompanyStocks stocks = companies.get(companyId);
        if (stocks.getQuantity() < quantity) {
            return false;
        }
        companies.set(companyId,
                new CompanyStocks(stocks.getCompanyName(), stocks.getPrice(), stocks.getQuantity() - quantity));
        return true;
    }

    @Override
    public boolean sellStocks(int companyId, int quantity) {
        if (!checkId(companyId))
            return false;
        CompanyStocks stocks = companies.get(companyId);
        companies.set(companyId,
                new CompanyStocks(stocks.getCompanyName(), stocks.getPrice(), stocks.getQuantity() + quantity));
        return true;
    }
}
