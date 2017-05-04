package loadbalancer;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;

public class ConnectServer implements HttpHandler {
	private LoadBalancer loadBalancer;
	
	public ConnectServer(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
	@Override
    public void handle(HttpExchange httpExchange) throws IOException {
    	String addr = httpExchange.getRemoteAddress().toString().split(":")[0];
    	Map<String, String> params = Exchanges.queryToMap(httpExchange.getRequestURI().getQuery());
    	
    	System.out.println(addr + ":" + params.get("port"));
        Exchanges.writeResponse(httpExchange, "accept");
        
        loadBalancer.addServer(addr, params.get("port"));
    }
}
