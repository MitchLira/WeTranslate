package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Map;

import org.json.JSONArray;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
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

		int requestID = Integer.parseInt(URLDecoder.decode(params.get("requestid"),"UTF-8"));
		
		JSONArray translationsJSON = Database.getTranslations(requestID);
		
		if (translationsJSON != null)
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, translationsJSON.toString());
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Error getting translations");
	}

}
