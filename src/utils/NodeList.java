package utils;

import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class NodeList implements Serializable {
	private int index;
	private List<NodeData> nodeList;
	
	public NodeList() {
		index = 0;
		nodeList = new LinkedList<>();
	}
	
	public void addNode(NodeData nd) {
		nodeList.add(nd);
	}
	
	public NodeData getNode() {
		boolean foundNode = false;
		NodeData nd = null;
		
		while (!foundNode) {
			if (nodeList.isEmpty()) {
				return null;
			}
			
			nd = nodeList.get(index);
			if (isServerAlive(nd)) {
				foundNode = true;
			}
			else {
				nodeList.remove(index);
				index--;
			}
			
			index = nodeList.isEmpty() ? 0 : (index + 1) % nodeList.size();
		}
		
		return nd;
	}
	
	public String toString() {
		String str = "";
		
		for (NodeData nd : nodeList) {
			str += nd.getLocation() + "\n";
		}
		
		return str;
	}
	
	public boolean isServerAlive(NodeData nd) {
		try ( Socket s = new Socket(nd.getAddr(), Integer.parseInt(nd.getPort())) ) {
	        return true;
	    } catch (Exception e) {}
	    return false;
    }
}
