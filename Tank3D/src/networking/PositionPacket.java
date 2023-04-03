package networking;

import java.util.LinkedList;

import org.jogamp.vecmath.Vector3f;

public class PositionPacket extends Packet {

	public String username;
	public Vector3f position;
	public LinkedList<User> users = new LinkedList<User>();
	
	public PositionPacket(String username, Vector3f position, LinkedList<User> users) {
		this.id = ID.POSITION;
		this.position = position;
		this.users = users;
		this.username = username;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + username + " " + position.x + " " + position.y + " " + position.z;
		for(User user : users) {
			data += " " + user.username + " " + user.tColor + " " + user.ipAddress + " " + user.port + " " + user.position.x + " " + user.position.y + " " + user.position.z;
		}
		return data.getBytes();
	}
	
}
