package demon1999.dao;

import demon1999.model.CompanyStocks;

import java.util.ArrayList;
import java.util.List;

public class ExchangerInMemoryDao implements ExchangerDao {
    private List<CompanyStocks> companies = new ArrayList<>();

    @Override
    public synchronized boolean checkId(int companyId) {
        return companyId >= 0 && companyId < companies.size();
    }

    @Override
    public synchronized int addCompany(String name) {
        int id = companies.size();
        companies.add(new CompanyStocks(name));
        return id;
    }

    @Override
    public synchronized void changePrice(int companyId, double newPrice) {
        companies.get(companyId).setPrice(newPrice);
    }

    @Override
    public synchronized double getPrice(int companyId) {
        return companies.get(companyId).getPrice();
    }

    @Override
    public synchronized int getQuantity(int companyId) {
        return companies.get(companyId).getQuantity();
    }

    @Override
    public synchronized void buyStocks(int companyId, int quantity) {
        companies.get(companyId).addQuantity(-quantity);
    }

    @Override
    public synchronized void sellStocks(int companyId, int quantity) {
        companies.get(companyId).addQuantity(quantity);
    }
}
