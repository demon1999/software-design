package demon1999;

import demon1999.dao.ExchangerDao;
import demon1999.dao.ExchangerInMemoryDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangerController {
    private ExchangerDao exchangerDao = new ExchangerInMemoryDao();
    @RequestMapping("/add_company")
    public int addCompany(String name) {
        return exchangerDao.addCompany(name);
    }

    @RequestMapping("/change_price")
    public boolean changePrice(int companyId, double newPrice) {
        if (!exchangerDao.checkId(companyId) || newPrice <= 0.0)
            return false;
        exchangerDao.changePrice(companyId, newPrice);
        return true;
    }

    @RequestMapping("/get_price")
    public Double getPrice(int companyId) {
        if (!exchangerDao.checkId(companyId))
            return -1.0;
        return exchangerDao.getPrice(companyId);
    }

    @RequestMapping("/get_quantity")
    public int getQuantity(int companyId) {
        if (!exchangerDao.checkId(companyId))
            return -1;
        return exchangerDao.getQuantity(companyId);
    }

    @RequestMapping("/buy_stocks")
    public boolean buyStocks(int companyId, int quantity) {
        if (!exchangerDao.checkId(companyId) ||
            exchangerDao.getQuantity(companyId) < quantity)
            return false;
        exchangerDao.buyStocks(companyId, quantity);
        return true;
    }

    @RequestMapping("/sell_stocks")
    public boolean sellStocks(int companyId, int quantity) {
        if (!exchangerDao.checkId(companyId) || quantity < 0)
            return false;
        exchangerDao.sellStocks(companyId, quantity);
        return true;
    }
}
