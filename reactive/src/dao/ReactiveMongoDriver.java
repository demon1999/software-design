package dao;

import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoDatabase;

import com.mongodb.rx.client.Success;
import rx.Observable;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.not;
import static dao.Currency.RUB;


public class ReactiveMongoDriver {
    private static MongoDatabase mongoDatabase;
    private static final String usersCollection = "users";
    private static final String itemsCollection = "items";

    public ReactiveMongoDriver(String url) {
        mongoDatabase = MongoClients.create(url).getDatabase("service");
    }

    public Observable<User> getUser(long id) {
        return mongoDatabase.getCollection(usersCollection)
                .find(eq("id", id))
                .toObservable()
                .map(User::new);
    }

    public Observable<Boolean> addUser(User user) {
        return getUser(user.getId())
                .singleOrDefault(null)
                .flatMap(existingUser -> {
                    if (existingUser != null) {
                        return Observable.just(false);
                    }

                    return mongoDatabase.getCollection(usersCollection)
                            .insertOne(user.toDocument())
                            .asObservable()
                            .isEmpty()
                            .map(it -> !it);
                });
    }

    public Observable<Boolean> deleteUser(long id) {
        return getUser(id)
                .singleOrDefault(null)
                .flatMap(existingUser -> {
                    if (existingUser == null) {
                        return Observable.just(false);
                    }

                    return mongoDatabase.getCollection(usersCollection)
                            .deleteOne(existingUser.toDocument())
                            .asObservable()
                            .map(it -> true);
                });
    }


    public Observable<Item> getItem(long id) {
        return mongoDatabase.getCollection(itemsCollection)
                .find(eq("id", id))
                .toObservable()
                .map(Item::new);
    }

    public Observable<Boolean> addItem(Item item) {
        return getItem(item.getId())
                .singleOrDefault(null)
                .flatMap(existingItem -> {
                    if (existingItem != null) {
                        return Observable.just(false);
                    }

                    return mongoDatabase.getCollection(itemsCollection)
                            .insertOne(item.toDocument())
                            .asObservable()
                            .isEmpty()
                            .map(it -> !it);
                });
    }

    public Observable<Boolean> deleteItem(long id) {
        return getItem(id)
                .singleOrDefault(null)
                .flatMap(existingItem -> {
                    if (existingItem == null) {
                        return Observable.just(false);
                    }

                    return mongoDatabase.getCollection(itemsCollection)
                            .deleteOne(existingItem.toDocument())
                            .asObservable()
                            .map(it -> true);
                });
    }

    public Observable<Item> getItemsForUser(long id) {
        return getUser(id).singleOrDefault(null)
                .map(user -> {
                    if (user == null)
                        return RUB;
                    return user.getCurrency();})
                .flatMap(currency -> mongoDatabase
                        .getCollection(itemsCollection)
                .find()
                .toObservable()
                .map(Item::new)
                .map(item -> {
                    return new Item(item.getId(), item.getName(),
                            currency,
                            item.getPrice(currency));
                }));
    }
}
