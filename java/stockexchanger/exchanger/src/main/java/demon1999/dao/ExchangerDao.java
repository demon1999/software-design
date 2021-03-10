package demon1999.dao;

public interface Dao {

    int addCompany(String name);

    boolean changePrice(int companyId, double newPrice);

    double getPrice(int companyId);

    int getQuantity(int companyId);

    boolean buyStocks(int companyId, int quantity);

    boolean sellStocks(int companyId, int quantity);

}
