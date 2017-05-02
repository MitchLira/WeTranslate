package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.*;

public class Server {

	public static void main(String[] args) throws IOException {
		int port;
		String portStr = System.getenv("PORT");
		
		if (portStr == null) port = 8080;
		else 				 port = Integer.parseInt(portStr);
		
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		System.out.println("Server running on " + server.getAddress());
		server.createContext("/test", new MyHandler());
		server.start();
	}

	static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "This is the response";
            Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            
            if (params.containsKey("name")) {
            	response += " Hey " + params.get("name") + "!";
            }
            writeResponse(httpExchange, response);
        }
    }
	
	
	public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
	    httpExchange.sendResponseHeaders(200, response.length());
	    OutputStream os = httpExchange.getResponseBody();
	    os.write(response.getBytes());
	    os.close();
	}


	/**
	 * returns the url parameters in a map
	 * @param query
	 * @return map
	 */
	public static Map<String, String> queryToMap(String query){
		Map<String, String> result = new HashMap<String, String>();
		
		if (query != null) {
			for (String param : query.split("&")) {
		        String pair[] = param.split("=");
		        if (pair.length>1) {
		            result.put(pair[0], pair[1]);
		        }else{
		            result.put(pair[0], "");
		        }
		    }
		}
	    
	    return result;
	}
}