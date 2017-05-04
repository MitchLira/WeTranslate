package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import com.sun.net.httpserver.*;


public class Server {
	
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
		
		HttpURLConnection.setFollowRedirects(true);
		HttpURLConnection connection = (HttpURLConnection) new URL("http://wetranslate.herokuapp.com:8000/connect?port=7001").openConnection();
		connection.setRequestMethod("POST");
		connection.getResponseCode();
		
		InputStream is = connection.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		
		
		System.out.println(baos.toString("UTF-8"));
	}
}