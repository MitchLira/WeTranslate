package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
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
		
		String username = params.get("username");
		String text = params.get("text");
		int requestID = Integer.parseInt(params.get("requestid"));
		
		if (Database.insertTranslation(username, text, requestID)){
			String user=Database.getRequestCreator(requestID);
			StringBuilder builder = new StringBuilder();
			builder.append("http://wetranslate.ddns.net:7000/notifyUser?");
			builder.append("username="); builder.append(user);

			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(builder.toString()).openConnection();
				connection.setRequestMethod("POST");
				connection.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, "Inserted translation");
		}
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_CONFLICT, "Error inserting translation");
	}
}
