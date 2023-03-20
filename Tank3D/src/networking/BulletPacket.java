package networking;

import java.util.LinkedList;

import org.jogamp.vecmath.Vector3f;

public class BulletPacket extends Packet {

	public LinkedList<User> users = new LinkedList<User>();
	public Vector3f position;
	public Vector3f target;
	public String username;
	
	public BulletPacket(String username, Vector3f position, Vector3f target, LinkedList<User> users) {
		this.username = username;
		this.position = position;
		this.target = target;
		this.users = users;
		this.id = ID.BULLET;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + username + " " + position.x + " " + position.y + " " + position.z + " " + target.x + " " + target.y + " " + target.z;
		for(User user : users) {
			data += " " + user.username + " " + user.ipAddress + " " + user.port;
		}
		return data.getBytes();
	}
}
