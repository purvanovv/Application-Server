package database;

import java.util.UUID;

public class User {
	private String id;

	private String username;
	
	private String password;

	public User() {
		String id = UUID.randomUUID().toString();
		this.setId(id);
	}
	
	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
		
}
