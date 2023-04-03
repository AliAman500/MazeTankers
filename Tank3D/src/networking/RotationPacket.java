package networking;

import java.util.LinkedList;

public class RotationPacket extends Packet {

	public boolean forwards;
	public boolean backwards;
	public float direction;
	public float turretDirection;
	public String username;
	
	public LinkedList<User> users = new LinkedList<User>();
	
	public RotationPacket(String username, boolean forwards, boolean backwards, float direction, float turretDirection, Room room) {
		this.id = ID.ROTATION;
		this.username = username;
		this.forwards = forwards;
		this.backwards = backwards;
		this.direction = direction;
		this.turretDirection = turretDirection;
		this.users = room.users;
	}
	
	public RotationPacket(String username, boolean forwards, boolean backwards, float direction, float turretDirection, LinkedList<User> users) {
		this.username = username;
		this.id = ID.ROTATION;
		this.forwards = forwards;
		this.backwards = backwards;
		this.direction = direction;
		this.turretDirection = turretDirection;
		this.users = users;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + username + " " + forwards + " " + backwards + " " + direction + " " + turretDirection;
		for(User user : users) {
			data += " " + user.username + " " + user.tColor + " " + user.ipAddress + " " + user.port + " " + user.position.x + " " + user.position.y + " " + user.position.z;
		}
		return data.getBytes();
	}
}