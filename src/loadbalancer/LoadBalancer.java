package loadbalancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;


public class LoadBalancer {
	private Map<String, String> nodes;
	HttpServer server;

	public static void main(String[] args) throws IOException {
		/*
		int port;
		String portStr = System.getenv("PORT");
		
		if (portStr == null) port = 8080;
		else 				 port = Integer.parseInt(portStr);
		*/
		
		int port = Integer.parseInt(System.getenv("PORT"));
		
		/*
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		System.out.println("Server running on " + server.getAddress());
		server.createContext("/test", new TestHandler());
		server.createContext("/connect", new ConnectServer());
		server.start();
		*/
		
		LoadBalancer loadBalancer = new LoadBalancer(port);
		loadBalancer.start();
	}
	
	public LoadBalancer(int port) throws IOException {
		this.nodes = new HashMap<>();
		this.server = HttpServer.create(new InetSocketAddress(port), 0);
		
		System.out.println("Server running on " + server.getAddress());
		server.createContext("/test", new TestHandler(this));
		server.createContext("/connect", new ConnectServer(this));
	}
	
	public void start() {
		server.start();
	}

	public void addServer(String addr, String port) {
		nodes.put(addr, port);
	}

	public String getServer() {
		for (Map.Entry<String, String> entry : nodes.entrySet()) {
			return entry.getKey() + ":" + entry.getValue();
		}
		
		return null;
	}
}
