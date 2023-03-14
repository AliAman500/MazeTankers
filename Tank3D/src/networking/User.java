package networking;

import org.jogamp.vecmath.Vector3f;

import enums.TankColor;

public class User {

	public String username;
	public String ipAddress;
	public int port;
	public Vector3f position;
	public TankColor tColor;
	
	public User(String username, TankColor tColor, String ipAddress, int port, Vector3f position) {
		this.username = username;
		this.tColor = tColor;
		this.ipAddress = ipAddress;
		this.port = port;
		this.position = position;
	}
	
	public boolean equals(User user) {
		return username.equals(user.username) && ipAddress.equals(user.ipAddress) && port == user.port;
	}
	
	public String toString() {
		return username + " " + ipAddress + " " + port;
	}
}
