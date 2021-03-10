package demon1999.model;

public class UserStocks {
    private int companyId;
    private int quantity;

    public UserStocks(int companyId) {
        this.companyId = companyId;
        this.quantity = 0;
    }

    public UserStocks(int companyId, double price, int quantity) {
        this.companyId = companyId;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void changeQuantity(int q) {
        quantity += q;
    }
}
