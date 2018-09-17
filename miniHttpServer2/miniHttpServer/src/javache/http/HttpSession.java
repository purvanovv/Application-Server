package javache.http;

import java.util.HashMap;

public interface HttpSession {
	String getId();
	
	boolean isValid();
	
	HashMap<String, Object> getAttributes();
	
	void addAtributes(String name,Object attribute);
	
	void invalidate();
}
