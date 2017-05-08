package database;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.Exchanges;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class InsertTranslationHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String url = "jdbc:sqlite:src/database/database.db";
            Connection conn = DriverManager.getConnection(url);
            Map<String,String> values= Exchanges.queryToMap(httpExchange.getRequestURI().getQuery());

            if(values.containsKey("translated_text") && values.containsKey("request_id") && values.containsKey("translated_user_id")){
                String query = this.queryConstructor(values);

                Statement stmt = conn.createStatement();
                if(stmt.executeUpdate(query)==1){
                    Exchanges.writeResponse(httpExchange,"Tradução introduzida com sucesso!");
                }
                else{
                    Exchanges.writeResponse(httpExchange,"Utilizador o pedido não existem!");
                }

                stmt.close();
            }
            else{
                Exchanges.writeResponse(httpExchange,"Faltam argumentos!");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String queryConstructor(Map<String,String> values){
        String query="INSERT INTO Translation(id,translated_text,request_id,translated_user_id) VALUES (null,";
        query+=values.get("translated_text")+",";
        query+=values.get("request_id")+",";
        query+=values.get("translation_user_id")+")";
        return query;
    }
}
