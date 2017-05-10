package loadbalancer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.Exchanges;

public class RedirectHandler implements HttpHandler {
	private LoadBalancer loadBalancer;
	private Set<String> acceptedRequestMethods;

	public RedirectHandler(LoadBalancer loadBalancer, String[] acceptedRequestMethods) {
		this.loadBalancer = loadBalancer;
		this.acceptedRequestMethods = new HashSet<>(Arrays.asList(acceptedRequestMethods));
	}
	
	@Override
	public void handle(HttpExchange exch) throws IOException {
		if (!exch.getRequestURI().getPath().equals(exch.getHttpContext().getPath())) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_NOT_FOUND, "Not found");
			return;
		}
		
		if (!acceptedRequestMethods.contains(exch.getRequestMethod())) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
			return;
		}
		
		String address = loadBalancer.getServer();
		String redirectPath = Exchanges.buildRedirectPath(exch, address);
		
		System.out.println("Redirecting to: " + redirectPath);
		Exchanges.redirectTo(exch, redirectPath);
	}

}
