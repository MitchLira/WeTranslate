package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import utils.HttpConnection;

import com.sun.net.httpserver.*;


public class Node {
	private HttpServer server;
	private int port;
	
	public static void main(String[] args) throws IOException {		
		int port = Integer.parseInt(args[0]);
		Node node = new Node(port);
		node.start();
	}
	
	
	public Node(int port) throws IOException {
		this.port = port;
		this.server = HttpServer.create(new InetSocketAddress(port), 0);
		
		System.out.println("Server running on " + server.getAddress());
		
		server.createContext("/test", new TestHandler());
		server.createContext("/insertUser", new InsertUser(new String[]{"email", "password"}));
		server.createContext("/insertRequest", new InsertRequest(new String[]{"email", "from", "to", "text"}));
		server.createContext("/insertTranslation", new InsertTranslation(new String[]{"email", "requestid", "text"}));
		
		/*
		server.createContext("/getRequests", new RedirectHandler(this, RequestMethod.getReadMethods()));
		server.createContext("/getTranslations", new RedirectHandler(this, RequestMethod.getReadMethods()));
		server.createContext("/connect", new ConnectServer(this));
		*/
	}
	
	public boolean start() throws IOException {
		server.start();
		
		/* Connect to Load Balancer */
		HttpURLConnection connection = (HttpURLConnection) new URL("http://wetranslate.ddns.net:7000/connect?port=" + port).openConnection();
		connection.setRequestMethod("POST");
		
		if (HttpConnection.getCode(connection) != HttpURLConnection.HTTP_ACCEPTED) {
			System.out.println("Node failed to start: " + HttpConnection.getMessage(connection));
			server.stop(0);
			return false;
		}
		
		return true;
	}
}