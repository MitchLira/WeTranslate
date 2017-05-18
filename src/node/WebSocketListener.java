package node;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketListener extends WebSocketClient {
	
	public WebSocketListener(URI serverURI) {
		super(serverURI);
	}

	public WebSocketListener(URI serverURI, Draft draft) {
		super(serverURI, draft);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
	}

	@Override
	public void onError(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void onMessage(String message) {
		System.out.println("Received: " + message);
	}

	@Override
	public void onOpen(ServerHandshake handshake) {
		System.out.println("Opened connection");		
	}
	
}
