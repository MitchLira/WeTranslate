package node.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.sql.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import database.Database;
import utils.Exchanges;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exch) throws IOException {
        String response = "What's up! Is it working? ";
        Map<String, String> params = Exchanges.queryToMap(exch);
        
        if (params.containsKey("name")) {
        	response += " Hey " + params.get("name") + "!";
        }
        
        try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sdis","sdis", "sdisdb");
			
			if (connection != null)
				System.out.println("Funcionou amigo");
			else
				System.out.println("Upsss");
			
			
			Database.getTranslations(1);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(response);
        
        Exchanges.writeResponse(exch, HttpURLConnection.HTTP_OK, response);
    }
}
