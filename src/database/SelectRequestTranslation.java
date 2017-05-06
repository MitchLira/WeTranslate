package database;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.Exchanges;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

public class SelectRequestTranslation implements HttpHandler{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String url = "jdbc:sqlite:src/database/database.db";

            Connection conn = DriverManager.getConnection(url);
            Map<String,String> values= Exchanges.queryToMap(httpExchange.getRequestURI().getQuery());

            if(values.containsKey("request_id")){
                String query = this.queryConstructor(values);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String translatedText = rs.getString("translated_text");
                    System.out.println("Tradução: "+translatedText);
                }

                stmt.close();
            }
            else{
                Exchanges.writeResponse(httpExchange,"Introduza todos os argumentos!");
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String queryConstructor(Map<String,String> values){
        return "SELECT * FROM Translation WHERE request_id="+values.get("request_id");
    }
}
