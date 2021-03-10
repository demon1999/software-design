package demon1999.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangerClient {
    final HttpClient client;
    final URI baseUri;

    public ExchangerClient() throws URISyntaxException {
        this.client = HttpClient.newHttpClient();
        this.baseUri = new URI("http://localhost:8080");
    }

    private int queryInt(URI uri) {
        HttpRequest req = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(res.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int addCompany(String name) {
        URI uri = baseUri.resolve(String.format("/add_company?name=%s", name));
        return queryInt(uri);
    }

    public double getPrice(int companyId) {
        URI uri = baseUri.resolve(String.format("/get_price?companyId=%d", companyId));
        HttpRequest req = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Double.parseDouble(res.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1.0;
    }

    public int getQuantity(int companyId) {
        URI uri = baseUri.resolve(String.format("/get_quantity?companyId=%d", companyId));
        return queryInt(uri);
    }

    private boolean queryBool(URI uri) {
        HttpRequest req = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean buyStocks(int companyId, int quantity) {
        URI uri = baseUri.resolve(String.format("/buy_stocks?companyId=%d&quantity=%d", companyId, quantity));
        return queryBool(uri);
    }

    public boolean sellStocks(int companyId, int quantity) {
        URI uri = baseUri.resolve(String.format("/sell_stocks?companyId=%d&quantity=%d", companyId, quantity));
        return queryBool(uri);
    }

    public boolean changePrice(int companyId, double newPrice) {
        URI uri = baseUri.resolve(String.format("/change_price?companyId=%d&newPrice=%f", companyId, newPrice));
        return queryBool(uri);
    }
}
