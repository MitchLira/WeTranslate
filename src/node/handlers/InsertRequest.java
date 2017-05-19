package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
import utils.Exchanges;

public class InsertRequest extends NodeHandler implements HttpHandler {

	public InsertRequest(String[] requiredParams) {
		super(requiredParams);
	}

	@Override
	public void handle(HttpExchange exch) throws IOException {
		Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
		if (!requestAcceptable(params)) {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Parameters do not match.");
			return;
		}

		String username = URLDecoder.decode(params.get("username"),"UTF-8");
		String from = URLDecoder.decode(params.get("from"),"UTF-8");
		String to = URLDecoder.decode(params.get("to"),"UTF-8");
		String text = URLDecoder.decode(params.get("text"),"UTF-8");
		
		if (Database.insertRequest(username, from, to, text))
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "Inserted request");
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_CONFLICT, "Error inserting request");
	}

}
