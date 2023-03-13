package networking;

import java.util.LinkedList;

public class RoomPacket extends Packet {

	public LinkedList<User> users = new LinkedList<User>();
	
	public RoomPacket(ID packetID, Room room) {
		this.users = room.users;
		this.id = packetID;
	}
	
	public RoomPacket(ID packetID, User user) {
		users.add(user);
		this.id = packetID;
	}
	
	public RoomPacket(ID packetID, LinkedList<User> users) {
		this.users = users;
		this.id = packetID;
	}
	
	public byte[] getData() {
		String data = id.name();
		for(User user : users) {
			data += " " + user.username + " " + user.ipAddress + " " + user.port;
		}
		return data.getBytes();
	}
}
