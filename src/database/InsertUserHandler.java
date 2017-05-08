package database;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.Exchanges;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

public class InsertUserHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String url = "jdbc:sqlite:src/database/database.db";

            Connection conn = DriverManager.getConnection(url);
            Map<String,String> values=Exchanges.queryToMap(httpExchange.getRequestURI().getQuery());

            if(values.containsKey("email") && values.containsKey("password")){
                String query = this.queryConstructor(values);

                Statement stmt = conn.createStatement();
                if(stmt.executeUpdate(query)==1){
                    Exchanges.writeResponse(httpExchange,"Utilizador introduzido com sucesso!");
                }
                else{
                    Exchanges.writeResponse(httpExchange,"Utilizador já existe!");
                }

                stmt.close();
            }
            else{
                Exchanges.writeResponse(httpExchange,"Não existe argumentos suficientes!");
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String queryConstructor(Map<String,String> values){
            String query="INSERT INTO User(id,email,password) VALUES (null,";
            query+=values.get("email")+",";
            query+=values.get("password")+")";
            return query;
    }
}
