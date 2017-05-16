package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
import utils.Exchanges;

public class InsertUser extends NodeHandler implements HttpHandler {

	public InsertUser(String[] requiredParams) {
		super(requiredParams);
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		Map<String, String> params = Exchanges.queryToMap(exch);
		if (!requestAcceptable(exch, params)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
			return;
		}
		
		String username = params.get("username");
		String password = params.get("password");
		
		if (Database.insertUser(username, password))	
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "Inserted user");
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_CONFLICT, "User already exists");
	}
}
