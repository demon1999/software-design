import com.sun.jna.platform.win32.Netapi32Util;
import demon1999.UserController;
import demon1999.client.ExchangerClient;
import demon1999.model.UserStocks;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserControllerTest {
    @ClassRule
    public static GenericContainer exchangerServer
            = new FixedHostPortGenericContainer("exchanger:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);

    private UserController controller;
    private ExchangerClient client;
    private int googleId, yaId, snapId;


    @Before
    public void before() {
        controller = new UserController();
        client = controller.getClient();
        googleId = client.addCompany("Google");
        yaId = client.addCompany("Yandex");
        snapId = client.addCompany("Snapchat");

        client.sellStocks(googleId, 50);
        client.changePrice(googleId, 5.0);

        client.sellStocks(yaId, 20);
        client.changePrice(yaId, 10.0);

        client.sellStocks(snapId, 10);
        client.changePrice(snapId, 10.5);
    }

    @Test
    public void testAddUser() {
        Assert.assertEquals(controller.addUser(0), true);
        Assert.assertEquals(controller.addUser(0), false);
        Assert.assertEquals(controller.addUser(1), true);
    }

    @Test
    public void testAddMoney() {
        controller.addUser(0);
        controller.addUser(1);
        Assert.assertTrue(controller.addMoney(0, 10.0));
        Assert.assertTrue(controller.addMoney(1, 5.0));
        Assert.assertFalse(controller.addMoney(2, 5.0));
        Assert.assertFalse(controller.addMoney(1, -5.0));
        Assert.assertEquals(controller.getBalance(0), 10.0, 1e-7);
        Assert.assertEquals(controller.getBalance(1), 5.0, 1e-7);
        Assert.assertEquals(controller.getBalance(2), -1.0, 1e-7);
    }

    @Test
    public void testBuyStocks() {
        controller.addUser(0);
        controller.addMoney(0, 200.0);
        Assert.assertFalse(controller.buyStocks(0, googleId, 50));
        Assert.assertFalse(controller.buyStocks(0, yaId, 21));
        Assert.assertTrue(controller.buyStocks(0, snapId, 8));
        Assert.assertFalse(controller.buyStocks(0, snapId, 3));
        Assert.assertTrue(controller.buyStocks(0, snapId, 2));
        Assert.assertEquals(controller.getBalance(0), 200.0, 1e-7);
        client.changePrice(snapId, 5.0);
        Assert.assertEquals(controller.getBalance(0), 145.0, 1e-7);
        Assert.assertEquals(controller.getBalance(1), -1.0, 1e-7);
    }

    @Test
    public void testGetBalance() {
        controller.addUser(0);
        controller.addMoney(0, 200.0);
        controller.addUser(1);
        controller.addMoney(1, 500.0);
        controller.buyStocks(0, yaId, 10);
        controller.buyStocks(1, googleId, 50);
        controller.buyStocks(1, yaId, 10);
        controller.buyStocks(1, snapId, 10);
        Assert.assertEquals(controller.getBalance(0), 200.0, 1e-7);
        Assert.assertEquals(controller.getBalance(1), 500.0, 1e-7);
        client.changePrice(googleId, 10.0);
        Assert.assertEquals(controller.getBalance(0), 200.0, 1e-7);
        Assert.assertEquals(controller.getBalance(1), 750.0, 1e-7);
        client.changePrice(yaId, 5.0);
        Assert.assertEquals(controller.getBalance(0), 150.0, 1e-7);
        Assert.assertEquals(controller.getBalance(1), 700.0, 1e-7);
    }

    @Test
    public void testGetStocks() {
        controller.addUser(0);
        controller.addMoney(0, 200.0);
        controller.addUser(1);
        controller.addMoney(1, 500.0);
        controller.buyStocks(0, yaId, 10);
        controller.buyStocks(1, googleId, 50);
        controller.buyStocks(1, yaId, 10);
        controller.buyStocks(1, snapId, 10);
        List<UserStocks> stocks0 = controller.getStocks(0);
        Assert.assertEquals(stocks0.size(), 1);
        Assert.assertEquals(stocks0.get(0).getQuantity(), 10);
        Assert.assertEquals(stocks0.get(0).getCompanyId(), yaId);
        List<UserStocks> stocks1 = controller.getStocks(1);
        boolean[] has = new boolean[3];
        Assert.assertEquals(stocks1.size(), 3);
        for (UserStocks s : stocks1) {
            if (s.getCompanyId() == yaId && !has[0] &&
                s.getQuantity() == 10) {
                has[0] = true;
            } else if (s.getCompanyId() == googleId && !has[1] &&
                s.getQuantity() == 50) {
                has[1] = true;
            } else if (s.getCompanyId() == snapId && !has[2] &&
                s.getQuantity() == 10) {
                has[2] = true;
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void testSellStocks() {
        controller.addUser(0);
        controller.addMoney(0, 200.0);
        controller.buyStocks(0, yaId, 10);
        Assert.assertFalse(controller.sellStocks(0, googleId, 1));
        Assert.assertFalse(controller.sellStocks(0, yaId, 11));
        Assert.assertTrue(controller.sellStocks(0, yaId, 5));
        Assert.assertFalse(controller.sellStocks(0, yaId, 6));
        client.changePrice(yaId, 5.0);
        Assert.assertEquals(controller.getBalance(0), 175.0, 1e-7);
    }
}
