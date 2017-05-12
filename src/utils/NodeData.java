package utils;

public class NodeData implements Comparable<NodeData> {
	private String addr;
	private String port;
	private String location;
	private int requestsTaken;
	
	public NodeData(String addr, String port) {
		this.addr = addr;
		this.port = port;
		this.location = addr + ":" + port;
		this.requestsTaken = 0;
	}

	public String getAddr() {
		return addr;
	}

	public String getPort() {
		return port;
	}

	public String getLocation() {
		return location;
	}
	
	public void increaseRequestsTaken() {
		this.requestsTaken++;
	}

	@Override
	public int compareTo(NodeData nd) {
		return Integer.compare(this.requestsTaken, nd.requestsTaken);
	}
}
