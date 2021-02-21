package server;

import dao.Currency;
import dao.Item;
import dao.ReactiveMongoDriver;
import dao.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Map;


//http://localhost:8080/register?id=100&currency=RUB
//http://localhost:8080/register?id=100&currency=RUB
//http://localhost:8080/register?id=101&currency=EURO
//http://localhost:8080/register?id=102&currency=USD
//http://localhost:8080/get_user?id=100
//http://localhost:8080/get_user?id=103
//http://localhost:8080/add_item?id=1&name=milk&currency=RUB&price=100
//http://localhost:8080/add_item?id=1&name=milk&currency=RUB&price=100
//http://localhost:8080/add_item?id=2&name=usd_milk&currency=USD&price=100
//http://localhost:8080/add_item?id=3&name=euro_milk&currency=EURO&price=100
//http://localhost:8080/get_item?id=2
//http://localhost:8080/get_item?id=4
//http://localhost:8080/catalog?id=100
//http://localhost:8080/catalog?id=101
//http://localhost:8080/catalog?id=102
//http://localhost:8080/delete_user?id=100
//http://localhost:8080/delete_user?id=101
//http://localhost:8080/delete_user?id=102
//http://localhost:8080/delete_item?id=1
//http://localhost:8080/delete_item?id=2
//http://localhost:8080/delete_item?id=3
public class Server {
    static ReactiveMongoDriver driver = new ReactiveMongoDriver("mongodb://localhost:27017");

    public static void main(final String[] args) {
        HttpServer
                .newServer(8080)
                .start((request, response) -> {

                    String action = request.getDecodedPath().substring(1);

                    Observable<String> responseMessage;
                    try {
                        responseMessage = process(action, request.getQueryParameters());
                    } catch (RuntimeException e) {
                        responseMessage = Observable.just(e.getMessage());
                        response.setStatus(HttpResponseStatus.BAD_REQUEST);
                    }

                    return response.writeString(responseMessage);
                })
                .awaitShutdown();
    }

    private static Observable<String> process(String query, Map<String, List<String>> queryParameters) {
        Observable<String> res;

        switch (query) {
            case "register":
                res = processRegister(queryParameters);
                break;
            case "catalog":
                res = processGetItems(queryParameters);
                break;
            case "get_user":
                res = processGetUser(queryParameters);
                break;
            case "delete_user":
                res = processDeleteUser(queryParameters);
                break;
            case "add_item":
                res = processAddItem(queryParameters);
                break;
            case "get_item":
                res = processGetItem(queryParameters);
                break;
            case "delete_item":
                res = processDeleteItem(queryParameters);
                break;
            default:
                throw new RuntimeException("Unexpected query");
        }

        return res;
    }

    public static Observable<String> processGetItems(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));
        return driver.getItemsForUser(id)
                .map(result ->
                        String.format("id: %d, name: %s, price: %.4f%s\n",
                                result.getId(), result.getName(),
                                result.getPrice(), result.getCurrency().toString()));
    }

    public static Observable<String> processDeleteItem(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));

        return driver.deleteItem(id)
                .map(result -> {
                            if (!result) {
                                return String.format("Item with id '%d' doesn't exists", id);
                            }
                            return String.format("Item with id '%d' successfully deleted", id);
                        }
                );

    }

    public static Observable<String> processGetItem(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));

        return driver.getItem(id)
                .singleOrDefault(null)
                .map(item -> {
                    if (item == null) {
                        return String.format("Item with id %d doesn't exist", id);
                    }
                    return String.format("Item with id %d has name %s, price %.4f%s",
                            id, item.getName(), item.getPrice(), item.getCurrency().toString());
                });
    }

    public static Observable<String> processAddItem(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));
        String name = queryParameters.get("name").get(0);
        Currency currency = Currency.valueOf(queryParameters.get("currency").get(0));
        double price = Double.parseDouble(queryParameters.get("price").get(0));

        return driver.addItem(new Item(id, name, currency, price))
                .map(result -> {
                            if (!result) {
                                return String.format("Item with id '%d' is already exists", id);
                            }
                            return String.format("Item with id '%d' successfully added", id);
                        }
                );

    }

    public static Observable<String> processDeleteUser(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));

        return driver.deleteUser(id)
                .map(result -> {
                            if (!result) {
                                return String.format("User with id '%d' doesn't exists", id);
                            }
                            return String.format("User with id '%d' successfully deleted", id);
                        }
                );

    }

    public static Observable<String> processGetUser(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));

        return driver.getUser(id).singleOrDefault(null)
                .map(user -> {
                    if (user == null) {
                        return String.format("User with id %d doesn't exist",
                                id);
                    }
                    return String.format("User with id %d has currency %s",
                            id, user.getCurrency().toString());
                });
    }

    public static Observable<String> processRegister(Map<String, List<String>> queryParameters) {
        long id = Long.parseLong(queryParameters.get("id").get(0));
        Currency currency = Currency.valueOf(queryParameters.get("currency").get(0));

        return driver.addUser(new User(id, currency))
                .map(result -> {
                            if (!result) {
                                return String.format("User with id '%d' is already exists", id);
                            }
                            return String.format("User with id '%d' successfully added", id);
                        }
                    );
    }
}
