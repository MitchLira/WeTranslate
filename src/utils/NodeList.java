package utils;

import java.util.LinkedList;
import java.util.List;

public class NodeList {
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
		if (nodeList.isEmpty())
			return null;
		
		NodeData nd = nodeList.get(index);
		index = (index + 1) % nodeList.size(); 
		return nd;
	}
}
