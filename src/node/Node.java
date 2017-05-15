package node;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import utils.HttpConnection;
import utils.RequestMethod;

import com.sun.net.httpserver.*;

import loadbalancer.handlers.RedirectHandler;
import node.api.UserExists;
import node.handlers.GetRequests;
import node.handlers.GetTranslations;
import node.handlers.InsertRequest;
import node.handlers.InsertTranslation;
import node.handlers.InsertUser;
import node.handlers.Login;
import node.handlers.TestHandler;


public class Node {
	public static String hostName = "wetranslate.ddns.net";
	private HttpServer server;
	private int port;
	
	public static void main(String[] args) throws IOException {		
		Options options = new Options();
		
		Option portOption = new Option("p", "port", true, "listening port");
		portOption.setRequired(true);
		options.addOption(portOption);
		
		Option localhostOption = new Option("localhost", false, "working on localhost");
		localhostOption.setRequired(false);
		options.addOption(localhostOption);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			formatter.printHelp("LoadBalancer", options);
			System.exit(1);
			return;
		}
		
		int port = Integer.parseInt(cmd.getOptionValue("port"));
		
		if (cmd.hasOption("localhost"))
			hostName = "localhost";
		
		Node node = new Node(port);
		node.start();
	}
	
	
	public Node(int port) throws IOException {
		this.port = port;
		this.server = HttpServer.create(new InetSocketAddress(port), 0);
		
		System.out.println("Server running on " + server.getAddress());
		
		server.createContext("/test", new TestHandler());
		server.createContext("/insertUser", new InsertUser(new String[]{"email", "password"}));
		server.createContext("/insertRequest", new InsertRequest(new String[]{"email", "from", "to", "text"}));
		server.createContext("/insertTranslation", new InsertTranslation(new String[]{"email", "requestid", "text"}));
		server.createContext("/getRequests", new GetRequests(new String[]{"from", "to"}));
		server.createContext("/getTranslations", new GetTranslations(new String[]{"requestid"}));
		
		server.createContext("/login", new Login(new String[]{"email", "password"}));
		server.createContext("/api/userExists", new UserExists(new String[]{"email"}));
	}
	
	public boolean start() throws IOException {
		server.start();
		
		/* Connect to Load Balancer */
		HttpURLConnection connection = (HttpURLConnection) new URL("http://" + hostName + ":7000/connect?port=" + port).openConnection();
		connection.setRequestMethod("POST");
		
		if (HttpConnection.getCode(connection) != HttpURLConnection.HTTP_ACCEPTED) {
			System.out.println("Node failed to start: " + HttpConnection.getMessage(connection));
			server.stop(0);
			return false;
		}
		
		return true;
	}
}