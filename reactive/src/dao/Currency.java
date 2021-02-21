package dao;

public enum Currency {
    RUB(1), EURO(80), USD(70);

    double rate;
    Currency(double rate) {
        this.rate = rate;
    }

    public static double convert(double price, Currency a, Currency b) {
        return price * a.rate / b.rate;
    }

}
