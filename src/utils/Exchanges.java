package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class Exchanges {
	
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
	
	public static void writeResponse(HttpExchange exch, int code, String response) throws IOException {
	    exch.sendResponseHeaders(code, response.length());
	    OutputStream os = exch.getResponseBody();
	    os.write(response.getBytes());
	    os.close();
	}
	
	public static void writeResponse(HttpExchange exch, int code) throws IOException {
		exch.sendResponseHeaders(code, 0);
	}
	
	public static void redirectTo(HttpExchange exch, String location) throws IOException {
		exch.getResponseHeaders().set("Location", location);
		exch.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);
	}

	public static String buildRedirectPath(HttpExchange exch, String address) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("http:/");
    	builder.append(address);
    	builder.append(exch.getRequestURI().getPath());
    	if (exch.getRequestURI().getQuery() != null) {
    		builder.append("?" + exch.getRequestURI().getQuery());
    	}
    	
    	return builder.toString();
	}
	
}
