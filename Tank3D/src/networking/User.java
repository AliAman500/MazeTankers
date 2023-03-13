package networking;

public class User {

	public String username;
	public String ipAddress;
	public int port;
	
	public User(String username, String ipAddress, int port) {
		this.username = username;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public boolean equals(User user) {
		return username == user.username && ipAddress == user.ipAddress && port == user.port;
	}
	
	public String toString() {
		return username + " " + ipAddress + " " + port;
	}
}
