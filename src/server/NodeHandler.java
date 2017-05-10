package server;

import java.util.Map;

public abstract class NodeHandler {
	protected String[] requiredParams;

	public NodeHandler(String[] requiredParams) {
		this.requiredParams = requiredParams;
	}	
	
	public boolean requestAcceptable(Map<String, String> params) {
		if (requiredParams.length != params.size())
			return false;
		
		for (String param : requiredParams) {
			if (!params.containsKey(param))
				return false;
		}
		
		return true;
	}
}
