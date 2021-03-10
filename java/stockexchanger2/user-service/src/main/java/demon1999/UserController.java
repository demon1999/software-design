package demon1999;

import demon1999.client.ExchangerClient;
import demon1999.dao.UsersDao;
import demon1999.dao.UsersInMemoryDao;
import demon1999.model.UserStocks;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private UsersDao usersDao;
    private ExchangerClient exchangerClient;

    public UserController() {
        usersDao = new UsersInMemoryDao();
        try {
            exchangerClient = new ExchangerClient();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ExchangerClient getClient() {
        return exchangerClient;
    }

    @RequestMapping("/add_user")
    public boolean addUser(int userId) {
        return usersDao.addUser(userId);
    }

    @RequestMapping("/add_money")
    public boolean addMoney(int userId, double money) {
        if (!usersDao.checkId(userId) || money < 0.0)
            return false;
        usersDao.addMoney(userId, money);
        return true;
    }

    @RequestMapping("/get_stocks")
    public List<UserStocks> getStocks(int userId) {
        if (!usersDao.checkId(userId)) {
            return new ArrayList<>();
        }
        return usersDao.getUserStocks(userId);
    }

    @RequestMapping("/get_balance")
    public double getBalance(int userId) {
        if (!usersDao.checkId(userId)) {
            return -1.0;
        }
        List<UserStocks> stocks = getStocks(userId);
        double balance = usersDao.getMoney(userId);
//        System.out.println(balance);
        for (UserStocks s : stocks) {
            if (exchangerClient.getPrice(s.getCompanyId()) > 0.0)
                balance += (double)(s.getQuantity()) * exchangerClient.getPrice(s.getCompanyId());
//            System.out.println("id: " + s.getCompanyId() + ", quantity: " + s.getQuantity() + ", balance: " + balance);
        }
//        System.out.println(balance);
        return balance;
    }

    @RequestMapping("/buy_stocks")
    public boolean buyStocks(int userId, int companyId, int quantity) {
        double price = exchangerClient.getPrice(companyId);
        int q = exchangerClient.getQuantity(companyId);
        if (!usersDao.checkId(userId) || quantity < 0 ||
            quantity > q || price * (double)quantity > usersDao.getMoney(userId)) {
            return false;
        }
        if (exchangerClient.buyStocks(companyId, quantity)) {
//            System.out.println("add money " + -price + " " + (double) quantity);
            usersDao.addMoney(userId, -price * (double) quantity);
            usersDao.addStocks(userId, companyId, quantity);
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/sell_stocks")
    public boolean sellStocks(int userId, int companyId, int quantity) {
        double price = exchangerClient.getPrice(companyId);
        if (!usersDao.checkId(userId) || usersDao.getQuantity(userId, companyId) < quantity) {
            return false;
        }
        if (exchangerClient.sellStocks(companyId, quantity)) {
            usersDao.addMoney(userId, price * (double) quantity);
            usersDao.removeStocks(userId, companyId, quantity);
            return true;
        } else {
            return false;
        }
    }
}
