package loadbalancer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Random;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import utils.NodeData;
import utils.NodeList;
import utils.RequestMethod;

import com.sun.net.httpserver.HttpServer;

import loadbalancer.handlers.ConnectServer;
import loadbalancer.handlers.NotifyHandler;
import loadbalancer.handlers.RedirectHandler;

import javax.net.ssl.*;

public class LoadBalancer {
	private NodeList nodes;
	private HttpsServer server;
	private WebSocketNotifier wsn;

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
	private ServerSocket messenger;

	public static void main(String[] args) {
		LoadBalancer loadBalancer = new LoadBalancer();
		loadBalancer.start();
	}
	
	public LoadBalancer() {
		this.nodes = new NodeList();
		
		Random r = new Random();
		while (!onCreate()) {
			try {
				Thread.sleep((r.nextInt(6) + 5) * 1000);		// Sleep between 5 and 10 secs
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Connecting to main LB");
			try {
				Socket socket = new Socket("wetranslate.ddns.net", 7002);
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				this.nodes = (NodeList) ois.readObject();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(this.nodes);
		}
	}
	
	public void start() {
		server.start();
		wsn.start();
		startMessenger();
	}

	public void addServer(String addr, String port) {
		nodes.addNode(new NodeData(addr, port));
	}

	public NodeData getServer() {
		return nodes.getNode();
	}
	
	
	private boolean onCreate() {
		try {

			initializeServer();
			this.wsn = new WebSocketNotifier(7001);
			this.messenger = new ServerSocket(7002);
			
			
			System.out.println("Server running on " + server.getAddress());
			
			server.createContext("/test", new RedirectHandler(this, RequestMethod.getReadMethods()));
			server.createContext("/insertUser", new RedirectHandler(this, RequestMethod.getInsertMethods()));
			server.createContext("/insertRequest", new RedirectHandler(this, RequestMethod.getInsertMethods()));
			server.createContext("/insertTranslation", new RedirectHandler(this, RequestMethod.getInsertMethods()));
			server.createContext("/getRequests", new RedirectHandler(this, RequestMethod.getReadMethods()));
			server.createContext("/getRequestsByUsername",new RedirectHandler(this,RequestMethod.getReadMethods()));
			server.createContext("/getTranslations", new RedirectHandler(this, RequestMethod.getReadMethods()));
			
			server.createContext("/login", new RedirectHandler(this, new String[]{RequestMethod.POST}));
			server.createContext("/api/userExists", new RedirectHandler(this, new String[]{RequestMethod.POST, RequestMethod.GET}));
			
			server.createContext("/connect", new ConnectServer(this));
			server.createContext("/notifyUser", new NotifyHandler(wsn));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private void initializeServer() throws  KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException  {
		// load certificate
		this.server = HttpsServer.create(new InetSocketAddress(7000), 0);
		String keystoreFilename = "server.keys";
		char[] keypass = "123456".toCharArray();
		FileInputStream fIn = new FileInputStream(keystoreFilename);
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(fIn, keypass);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(keystore, keypass);

		SSLContext sslContext=SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(),null,null);
		server.setHttpsConfigurator(new HttpsConfigurator(sslContext));
	}
	
	private void startMessenger() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Messenger started");
				
				try {
					while (true) {
						Socket socket = messenger.accept();
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(nodes);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
