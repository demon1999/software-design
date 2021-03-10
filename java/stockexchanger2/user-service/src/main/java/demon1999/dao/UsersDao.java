package demon1999.dao;

import demon1999.model.UserStocks;

import java.util.List;

public interface UsersDao {
    boolean checkId(int userId);
    boolean addUser(int userId);
    void addMoney(int userId, double money);
    double getMoney(int userId);
    List<UserStocks> getUserStocks(int userId);
    void addStocks(int userId, int companyId, int quantity);
    void removeStocks(int userId, int companyId, int quantity);
    int getQuantity(int userId, int companyId);
}
