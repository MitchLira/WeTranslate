package node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import utils.HttpConnection;

import com.sun.net.httpserver.*;

import node.api.UserExists;
import node.handlers.GetRequestByUsername;
import node.handlers.GetRequests;
import node.handlers.GetTranslations;
import node.handlers.InsertRequest;
import node.handlers.InsertTranslation;
import node.handlers.InsertUser;
import node.handlers.Login;
import node.handlers.TestHandler;

import javax.net.ssl.*;


public class Node {
	public static String hostName = "wetranslate.ddns.net";
	private HttpsServer server;
	private int port;
	private SSLContext sslContext;

	static {
		//for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier(){

					public boolean verify(String hostname,
										  javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true;
						}
						return false;
					}
				});
	}
	
	public static void main(String[] args) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
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
	
	
	public Node(int port) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		this.port = port;
		initializeServer();
		
		System.out.println("Server running on " + server.getAddress());
		
		server.createContext("/test", new TestHandler());
		server.createContext("/insertUser", new InsertUser(new String[]{"username", "password"}));
		server.createContext("/insertRequest", new InsertRequest(new String[]{"username", "from", "to", "text"}));
		server.createContext("/insertTranslation", new InsertTranslation(new String[]{"username", "requestid", "text"}));
		server.createContext("/getRequests", new GetRequests(new String[]{"from", "to"}));
		server.createContext("/getRequestsByUsername", new GetRequestByUsername(new String[]{"username"}));
		server.createContext("/getTranslations", new GetTranslations(new String[]{"requestid"}));

		server.createContext("/login", new Login(new String[]{"username", "password"}));
		server.createContext("/api/userExists", new UserExists(new String[]{"username"}));
	}
	
	public boolean start() throws IOException {
		server.start();
		
		/* Connect to Load Balancer */
		HttpsURLConnection connection = (HttpsURLConnection) new URL("https://" + hostName + ":7000/connect?port=" + port).openConnection();
		connection.setSSLSocketFactory(sslContext.getSocketFactory());
		connection.setRequestMethod("POST");

		if (HttpConnection.getCode(connection) != HttpsURLConnection.HTTP_ACCEPTED) {
			System.out.println("Node failed to start: " + HttpConnection.getMessage(connection));
			server.stop(0);
			return false;
		}
		
		return true;
	}

	private void initializeServer() throws FileNotFoundException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
		// load certificate
		this.server = HttpsServer.create(new InetSocketAddress(port), 0);
		char[] password = "123456".toCharArray();
		FileInputStream fKeys = new FileInputStream( "server.keys");
		FileInputStream fStore=new FileInputStream("truststore");
		KeyStore keystore = KeyStore.getInstance("JKS");
		KeyStore truststore=KeyStore.getInstance("JKS");
		keystore.load(fKeys, password);
		truststore.load(fStore,password);


		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(truststore);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(keystore, password);


		sslContext=SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),null);

		server.setHttpsConfigurator(new HttpsConfigurator(sslContext));
	}
}