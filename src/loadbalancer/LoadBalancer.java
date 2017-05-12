package loadbalancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import utils.NodeData;
import utils.NodeList;
import utils.RequestMethod;

import com.sun.net.httpserver.HttpServer;


public class LoadBalancer {
	private NodeList nodes;
	HttpServer server;

	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(args[0]);
		
		LoadBalancer loadBalancer = new LoadBalancer(port);
		loadBalancer.start();
	}
	
	public LoadBalancer(int port) throws IOException {
		this.nodes = new NodeList();
		this.server = HttpServer.create(new InetSocketAddress(port), 0);
		
		System.out.println("Server running on " + server.getAddress());
		server.createContext("/test", new RedirectHandler(this, RequestMethod.getReadMethods()));
		server.createContext("/insertUser", new RedirectHandler(this, RequestMethod.getInsertMethods()));
		server.createContext("/insertRequest", new RedirectHandler(this, RequestMethod.getInsertMethods()));
		server.createContext("/insertTranslation", new RedirectHandler(this, RequestMethod.getInsertMethods()));
		server.createContext("/getRequests", new RedirectHandler(this, RequestMethod.getReadMethods()));
		server.createContext("/getTranslations", new RedirectHandler(this, RequestMethod.getReadMethods()));
		server.createContext("/connect", new ConnectServer(this));
	}
	
	public void start() {
		server.start();
	}

	public void addServer(String addr, String port) {
		nodes.addNode(new NodeData(addr, port));
	}

	public NodeData getServer() {
		return nodes.getNode();
	}
}
