package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import utils.Exchanges;

public class InsertTranslation extends NodeHandler implements HttpHandler {

	public InsertTranslation(String[] requiredParams) {
		super(requiredParams);
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
		if (!requestAcceptable(params)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
			return;
		}
		
		String email = params.get("email");
		String text = params.get("text");
		int requestID = Integer.parseInt(params.get("requestid"));
		
		if (Database.insertTranslation(email, text, requestID))
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "Inserted translation");
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_CONFLICT, "Error inserting translation");
	}
}
