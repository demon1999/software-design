package demon1999.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProperty {
    private double money;
    private Map<Integer, UserStocks> stocks;

    public UserProperty() {
        money = 0;
        stocks = new HashMap<>();
    }

    public double getMoney() {
        return money;
    }

    public void changeQuantity(int companyId, int quantity) {
        if (!stocks.containsKey(companyId)){
            stocks.put(companyId, new UserStocks(companyId));
        }
        stocks.get(companyId).changeQuantity(quantity);
        if (stocks.get(companyId).getQuantity() == 0) {
            stocks.remove(companyId);
        }
    }

    public void addMoney(double delta) {
        money += delta;
//        System.out.println(delta);
    }

    public List<UserStocks> getUserStocks() {
        return new ArrayList<>(stocks.values());
    }

    public int getQuantity(int companyId) {
        if (stocks.containsKey(companyId))
            return stocks.get(companyId).getQuantity();
        return 0;
    }
}
