package demon1999.model;

public class CompanyStocks {
    private String companyName;
    private int quantity;
    private double price;

    public CompanyStocks(String companyName) {
        this.companyName = companyName;
        this.price = 0.0;
        this.quantity = 0;
    }

    public CompanyStocks(String companyName, double price, int quantity) {
        this.companyName = companyName;
        this.price = price;
        this.quantity = quantity;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCompanyName() {
        return companyName;
    }
}
