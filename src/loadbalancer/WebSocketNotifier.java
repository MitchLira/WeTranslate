package loadbalancer;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import database.Database;

public class WebSocketNotifier extends WebSocketServer {
	private Map<String, WebSocket> userMap;
	
	public WebSocketNotifier(int port) {
		super(new InetSocketAddress(port));
		userMap = new HashMap<>();
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println(conn + " closed connection");
		userMap.values().removeAll(Collections.singleton(conn));
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		e.printStackTrace();
	}

	@Override
	public void onMessage(WebSocket conn, String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			String username = jsonObject.getString("username");
			String key = jsonObject.getString("key");
			
			String realKey = Database.getUserKey(username);
			
			if (realKey == null || !key.equals(realKey))
				conn.close();
			else
				userMap.put(username, conn);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {}

	@Override
	public void onStart() {}

	
	public void sendToAll( String text ) {
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( text );
			}
		}
	}
	
	public void sendToUser(String username, String text) {
		WebSocket conn = userMap.get(username);
		if (conn != null)
			conn.send(text);
	}
}
