package loadbalancer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;
import utils.RequestMethod;

public class ConnectServer implements HttpHandler {
	private LoadBalancer loadBalancer;
	
	public ConnectServer(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}
	
	@Override
    public void handle(HttpExchange exch) throws IOException {
		if (!Arrays.asList(RequestMethod.getInsertMethods()).contains(exch.getRequestMethod())) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
		}
		
    	String addr = exch.getRemoteAddress().toString().split(":")[0];
    	Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
    	
    	System.out.println(addr + ":" + params.get("port"));
    	
    	loadBalancer.addServer(addr, params.get("port"));
        Exchanges.writeResponse(exch, HttpURLConnection.HTTP_ACCEPTED, "accept");
    }
}
