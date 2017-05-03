package loadbalancer;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    	StringBuilder builder = new StringBuilder();
    	
    	builder.append("http://localhost:7001/test");
    	if (httpExchange.getRequestURI().getQuery() != null) {
    		builder.append("?" + httpExchange.getRequestURI().getQuery());
    	}
    	
        Exchanges.redirectTo(httpExchange, builder.toString());
        httpExchange.close();
    }
}
