package demon1999.dao;

public interface ExchangerDao {

    boolean checkId(int companyId);

    int addCompany(String name);

    void changePrice(int companyId, double newPrice);

    double getPrice(int companyId);

    int getQuantity(int companyId);

    void buyStocks(int companyId, int quantity);

    void sellStocks(int companyId, int quantity);

}
