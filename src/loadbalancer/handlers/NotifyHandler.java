package loadbalancer.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import loadbalancer.LoadBalancer;
import loadbalancer.WebSocketNotifier;
import utils.Exchanges;
import utils.RequestMethod;

public class NotifyHandler implements HttpHandler {
	private WebSocketNotifier wsn;
	
	public NotifyHandler(WebSocketNotifier wsn) {
		this.wsn = wsn;
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		if (!exch.getRequestMethod().equals(RequestMethod.POST)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
			return;
		}
		
		Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
		
		if (params.containsKey("username")) {
			wsn.sendToUser(params.get("username"), "notify");
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "ok");
			return;
		}
		
		Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
	}

}
