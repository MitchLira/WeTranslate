package loadbalancer;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Exchanges.redirectTo(httpExchange, "http://localhost:7001/test");
        httpExchange.close();
    }
}
