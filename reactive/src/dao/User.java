package dao;

import org.bson.Document;

public class User {
    private final long id;
    private final Currency currency;

    public User(long id, Currency currency) {
        this.id = id;
        this.currency = currency;
    }

    public User(Document doc) {
        this(doc.getLong("id"), Currency.valueOf(doc.getString("currency")));
    }

    public Document toDocument() {
        return new Document("id", id).
                append("currency", currency.toString());
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getId() {
        return id;
    }

}
