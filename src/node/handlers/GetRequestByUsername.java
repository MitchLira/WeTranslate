package node.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import node.NodeHandler;
import org.json.JSONArray;
import utils.Exchanges;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.Map;

public class GetRequestByUsername extends NodeHandler implements HttpHandler{
    public GetRequestByUsername(String[] requiredParams) {
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

        JSONArray requestsJSON = Database.getRequestsByUsername(username);

        if (requestsJSON != null)
            Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, requestsJSON.toString());
        else
            Exchanges.writeResponse(exch, HttpURLConnection.HTTP_BAD_REQUEST, "Error getting requests");
    }
}
