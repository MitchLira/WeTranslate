package database;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DatabaseConnection {

    public static void main(String args[]){
        new DatabaseConnection(Integer.parseInt(args[0]));
    }

    public DatabaseConnection(int port){

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/insertUser",new InsertUserHandler());
            server.createContext("/insertRequest",new InsertRequestHandler());
            server.createContext("/insertTranslation",new InsertTranslationHandler());
            server.createContext("/selectRequestTranslation",new SelectRequestTranslation());
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
