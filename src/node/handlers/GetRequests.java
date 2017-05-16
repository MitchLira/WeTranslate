package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import org.json.JSONArray;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
import utils.Exchanges;

public class GetRequests extends NodeHandler implements HttpHandler {

	public GetRequests(String[] requiredParams) {
		super(requiredParams);
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		Map<String, String> params = Exchanges.queryToMap(exch);
		if (!requestAcceptable(exch, params)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
			return;
		}
		
		String source = params.get("from");
		String target = params.get("to");
		
		JSONArray requestsJSON = Database.getRequests(source, target);
		
		if (requestsJSON != null)
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, requestsJSON.toString());
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Error getting requests");
	}

}
