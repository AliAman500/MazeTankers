package networking;

import java.util.LinkedList;

import org.jogamp.vecmath.Vector3f;

public class BulletPacket extends Packet {

	public LinkedList<User> users = new LinkedList<User>();
	public Vector3f position;
	public float direction;
	public String username;
	
	public BulletPacket(String username, Vector3f position, float direction, LinkedList<User> users) {
		this.username = username;
		this.position = position;
		this.direction = direction;
		this.users = users;
		this.id = ID.BULLET;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + username + " " + position.x + " " + position.y + " " + position.z + " " + direction;
		for(User user : users) {
			data += " " + user.username + " " + user.ipAddress + " " + user.port;
		}
		return data.getBytes();
	}
}
