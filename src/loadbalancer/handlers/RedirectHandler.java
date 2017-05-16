package loadbalancer.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import loadbalancer.LoadBalancer;
import loadbalancer.LoginWorker;
import loadbalancer.Worker;
import utils.Exchanges;
import utils.RequestMethod;

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
		
		if (!validUser(exch)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_UNAUTHORIZED, "No user information");
			return;
		}
		
		String location = loadBalancer.getServer().getLocation();
		String redirectPath = Exchanges.buildRedirectPath(exch, location);
		
		System.out.println("Worker redirecting to: " + redirectPath);
		dispatchWorker(exch, redirectPath);
	}
	
	
	public boolean validUser(HttpExchange exch) {
		if (!exch.getRequestHeaders().containsKey("username"))
			return false;
		
		return true;
	}
	
	public void dispatchWorker(HttpExchange exch, String path) throws MalformedURLException, IOException {
		switch (exch.getRequestURI().getPath()) {
		case "/login":
			new LoginWorker(exch, path).start();
			break;
		default:
			new Worker(exch, path).start();
		}
	}
}
