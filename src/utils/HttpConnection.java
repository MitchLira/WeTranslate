package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HttpConnection {
	public static String getMessage(HttpsURLConnection connection) throws IOException {
		InputStream is = connection.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		
		return baos.toString();
	}
	
	public static int getCode(HttpsURLConnection connection) throws IOException {
		return connection.getResponseCode();
	}
}
