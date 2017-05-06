package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class Exchanges {
	
	
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
	
	
	public static void redirectTo(HttpExchange httpExchange, String location) throws IOException {
		httpExchange.getResponseHeaders().set("Location", location);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);
	}
	
}
