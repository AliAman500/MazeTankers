package networking;

import java.util.LinkedList;

public class PositionPacket extends Packet {

	public boolean forwards;
	public boolean backwards;
	public float direction;
	public String username; 
	
	public LinkedList<User> users = new LinkedList<User>();
	
	public PositionPacket(String username, boolean forwards, boolean backwards, float direction, Room room) {
		this.id = ID.POSITION;
		this.username = username;
		this.forwards = forwards;
		this.backwards = backwards;
		this.direction = direction;
		this.users = room.users;
	}
	
	public PositionPacket(String username, boolean forwards, boolean backwards, float direction, LinkedList<User> users) {
		this.username = username;
		this.id = ID.POSITION;
		this.forwards = forwards;
		this.backwards = backwards;
		this.direction = direction;
		this.users = users;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + username + " " + forwards + " " + backwards + " " + direction;
		for(User user : users) {
			data += " " + user.username + " " + user.tColor + " " + user.ipAddress + " " + user.port + " " + user.position.x + " " + user.position.y + " " + user.position.z;
		}
		return data.getBytes();
	}
}
