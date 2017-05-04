package loadbalancer;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;

public class TestHandler implements HttpHandler {
	private LoadBalancer loadBalancer;
	
	public TestHandler(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    	StringBuilder builder = new StringBuilder();
    	
    	String addr_port = loadBalancer.getServer();
    	//httpExchange.getRemoteAddress().toString().split(":")[0];
    	
    	builder.append("http://");
    	builder.append(addr_port);
    	builder.append("/test");
    	if (httpExchange.getRequestURI().getQuery() != null) {
    		builder.append("?" + httpExchange.getRequestURI().getQuery());
    	}
    	
    	//httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
    	
    	System.out.println(httpExchange.getRemoteAddress());
    	System.out.println(httpExchange.getHttpContext());
    	
        Exchanges.redirectTo(httpExchange, builder.toString());
    }
}
