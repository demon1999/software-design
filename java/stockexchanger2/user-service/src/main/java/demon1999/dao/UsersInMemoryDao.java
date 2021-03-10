package demon1999.dao;

import demon1999.model.UserProperty;
import demon1999.model.UserStocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersInMemoryDao implements UsersDao {
    Map<Integer, UserProperty> usersProperty = new HashMap<>();

    @Override
    public synchronized boolean checkId(int userId) {
        if (usersProperty.containsKey(userId)) return true;
        return false;
    }

    @Override
    public synchronized boolean addUser(int userId) {
        if (!usersProperty.containsKey(userId)) {
            usersProperty.put(userId, new UserProperty());
            return true;
        }
        return false;
    }

    @Override
    public synchronized void addMoney(int userId, double money) {
        usersProperty.get(userId).addMoney(money);
    }

    @Override
    public synchronized double getMoney(int userId) {
        return usersProperty.get(userId).getMoney();
    }

    @Override
    public synchronized List<UserStocks> getUserStocks(int userId) {
        return usersProperty.get(userId).getUserStocks();
    }

    @Override
    public synchronized void addStocks(int userId, int companyId, int quantity) {
        usersProperty.get(userId).changeQuantity(companyId, quantity);
    }

    @Override
    public synchronized void removeStocks(int userId, int companyId, int quantity) {
        usersProperty.get(userId).changeQuantity(companyId, -quantity);
    }

    @Override
    public synchronized int getQuantity(int userId, int companyId) {
        return usersProperty.get(userId).getQuantity(companyId);
    }
}
