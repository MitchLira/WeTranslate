package node.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
import utils.Exchanges;

public class UserExists extends NodeHandler implements HttpHandler {

	public UserExists(String[] requiredParams) {
		super(requiredParams);
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
		if (!requestAcceptable(params)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
			return;
		}
		
		String username = params.get("username");
		String password = params.get("password");
		
		if (Database.userAlreadyExists(username))	
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "User exists");
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_NOT_FOUND, "User doesnt exist");
		
	}
	
}
