package loadbalancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;


public class LoadBalancer {

	public static void main(String[] args) throws IOException {
		/*
		int port;
		String portStr = System.getenv("PORT");
		
		if (portStr == null) port = 8080;
		else 				 port = Integer.parseInt(portStr);
		*/
		
		int port = Integer.parseInt(args[0]);
		
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		System.out.println("Server running on " + server.getAddress());
		server.createContext("/test", new TestHandler());
		server.start();
	}
}
