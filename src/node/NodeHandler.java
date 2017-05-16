package node;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.sun.net.httpserver.*;

public abstract class NodeHandler {
	protected String[] requiredParams;

	public NodeHandler(String[] requiredParams) {
		this.requiredParams = requiredParams;
	}	
	
	public boolean requestAcceptable(HttpExchange exch, Map<String, String> params) throws UnknownHostException {
		String requestAddress = exch.getRemoteAddress().getAddress().getHostAddress();
		String serverAddress = InetAddress.getByName("wetranslate.ddns.net").getHostAddress();
		
		if (!requestAddress.equals(serverAddress))
			return false;
		
		if (requiredParams.length != params.size())
			return false;
		
		for (String param : requiredParams) {
			if (!params.containsKey(param))
				return false;
		}
		
		return true;
	}
}
