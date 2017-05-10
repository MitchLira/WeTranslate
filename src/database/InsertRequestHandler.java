package database;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import sun.net.www.protocol.http.HttpURLConnection;
import utils.Exchanges;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class InsertRequestHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String url = "jdbc:sqlite:src/database/database.db";
            Connection conn = DriverManager.getConnection(url);
            Map<String,String> values= Exchanges.queryToMap(httpExchange.getRequestURI().getQuery());

            if(values.containsKey("field_text") && values.containsKey("translate_from") && values.containsKey("translate_to") && values.containsKey("user_id")){
                String query = this.queryConstructor(values);

                Statement stmt = conn.createStatement();
                if(stmt.executeUpdate(query) == 1) {
                    Exchanges.writeResponse(httpExchange, HttpURLConnection.HTTP_OK, "Pedido introduzido com sucesso!");
                }
                else{
                    Exchanges.writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, "Utilizador não existe existe!");
                }

                stmt.close();
            }
            else{
                Exchanges.writeResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, "Faltam argumentos!");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String queryConstructor(Map<String,String> values){
        String query="INSERT INTO Request(id,field_text,translate_from,translate_to,user_id,translation_id) VALUES (null,";
        query+=values.get("field_text")+",";
        query+=values.get("translate_from")+",";
        query+=values.get("translate_to")+",";
        query+=values.get("user_id")+",-1)"; //para já translation_id inicialmente quando não há ainda tradução é -1
        return query;
    }
}
