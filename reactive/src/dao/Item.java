package dao;

import org.bson.Document;


public class Item {
    private final long id;
    private final String name;
    private final Currency currency;
    private final double price;

    public Item(long id, String name, Currency currency, double price) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.price = price;
    }


    public Item(Document doc) {
        this(doc.getLong("id"), doc.getString("name"),
             Currency.valueOf(doc.getString("currency")),
             doc.getDouble("price"));
    }

    public Document toDocument() {
        return new Document("id", id).
                append("name", name).
                append("price", price).
                append("currency", currency.toString());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getPrice(Currency cur) {
        return Currency.convert(price, currency, cur);
    }
}
