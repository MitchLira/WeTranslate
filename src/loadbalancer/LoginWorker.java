package loadbalancer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import loadbalancer.metadata.Metadata;
import utils.Exchanges;

public class LoginWorker extends Worker {

	public LoginWorker(HttpExchange exch, String path) throws MalformedURLException, IOException {
		super(exch, path);
	}

	@Override
	public void start() {
		super.start();
		
		try {
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Map<String, String> params = Exchanges.queryToMap(exch);
				Metadata.instance().getUserAddresses().put(params.get("username"), exch.getRemoteAddress().getAddress().getHostAddress());
				
				System.out.println(Metadata.instance().getUserAddresses());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
