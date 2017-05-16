package loadbalancer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.sun.net.httpserver.*;

import utils.Exchanges;


public class Worker {
	protected HttpExchange exch;
	protected String path;
	protected HttpURLConnection connection;
	
	public Worker(HttpExchange exch, String path) throws MalformedURLException, IOException {
		this.exch = exch;
		this.path = path;
		this.connection = (HttpURLConnection) new URL(path).openConnection();
	}
	
	public void start() {
		new Thread(() -> {
			try {
				int code = connection.getResponseCode();
				if (code < 400) {
					String response = getResponse(connection.getInputStream());
					Exchanges.writeResponse(exch, code, response);
				} else {
					String response = getResponse(connection.getErrorStream());
					Exchanges.writeResponse(exch, code, response);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	
	
	private String getResponse(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		int nRead = 0;
		byte[] bytes = new byte[1024];
		
		while ((nRead = is.read(bytes, 0, bytes.length)) != -1)
			baos.write(bytes, 0, nRead);
		
		return baos.toString();
	}
}
