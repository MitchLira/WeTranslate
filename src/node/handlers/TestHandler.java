package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import utils.Exchanges;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exch) {
        String response = "What's up! Is it working? ";
        Map<String, String> params = Exchanges.queryToMap(exch.getRequestURI().getQuery());
        
        if (params.containsKey("name")) {
        	response += " Hey " + params.get("name") + "!";
        }
        
        try {
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
