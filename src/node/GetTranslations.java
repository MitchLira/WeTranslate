package node;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import utils.Exchanges;

public class GetTranslations extends NodeHandler implements HttpHandler {

	public GetTranslations(String[] requiredParams) {
		super(requiredParams);
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
		if (!requestAcceptable(params)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
			return;
		}

		int requestID = Integer.parseInt(params.get("requestid"));
		
		if (Database.getTranslations(requestID))
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "Aqui coisas");
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Error getting translations");
	}

}