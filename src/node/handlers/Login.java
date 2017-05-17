package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
import utils.Exchanges;

public class Login extends NodeHandler implements HttpHandler {

	public Login(String[] requiredParams) {
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
		
		if (Database.validUser(username, password))	{
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "Signed in");
			Database.insertLogin(username, exch.getRemoteAddress().getAddress().getHostAddress());
		}
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_NOT_FOUND, "User not found");
		
	}

}
