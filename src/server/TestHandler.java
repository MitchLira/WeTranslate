package server;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "What's up! Is it working? ";
        Map<String, String> params = Exchanges.queryToMap(httpExchange.getRequestURI().getQuery());
        
        if (params.containsKey("name")) {
        	response += " Hey " + params.get("name") + "!";
        }
        
        Exchanges.writeResponse(httpExchange, response);
    }
}
