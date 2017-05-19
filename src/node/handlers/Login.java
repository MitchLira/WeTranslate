package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

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
			
			String key = null;
			try {
				String toDigest = username + password + exch.getRemoteAddress().getAddress().getHostAddress();
				byte[] hash = MessageDigest.getInstance("SHA-256").digest(toDigest.getBytes());
				key = (new HexBinaryAdapter()).marshal(hash).replaceAll("\\x00", "");
				
				Database.insertKey(username, key);
			} catch (NoSuchAlgorithmException e) {}
			
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, key);
		}
		else
			Exchanges.writeResponse(exch, HttpURLConnection.HTTP_NOT_FOUND, "User not found");
		
	}

}
