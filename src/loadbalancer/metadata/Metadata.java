package loadbalancer.metadata;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Metadata {
	Map<String, String> userAddresses;
	
	private static class Holder {
		private static final Metadata INSTANCE = new Metadata();
	}
	
	public static Metadata instance() {
		return Holder.INSTANCE;
	}
	
	private Metadata() {
		userAddresses = new HashMap<>();
	}
	
	public Map<String, String> getUserAddresses() {
		return userAddresses;
	}
}
