package networking;

import java.net.DatagramPacket;
import java.util.LinkedList;

public class Packet {

	public ID id;
	
	public enum ID {
		PLAY_REQUEST, CREATE_ROOM, JOIN_ROOM, RUN_GAME;
	}
	
	public static Packet parse(DatagramPacket dataPacket) {
		byte[] dataBytes = dataPacket.getData();
		int offset = dataPacket.getOffset();
		int length = dataPacket.getLength();
		String dataString = new String(dataBytes, offset, length);
		String data[] = dataString.split(" ");
		
		ID packetID = ID.valueOf(data[0]);
		LinkedList<User> users;
		switch(packetID) {
		case PLAY_REQUEST:
			String username = data[1];
			return new PlayReqPacket(username);
		case CREATE_ROOM:
			User user = new User(data[1], data[2], Integer.parseInt(data[3]));
			return new RoomPacket(ID.CREATE_ROOM, user);
		case JOIN_ROOM:
			users = new LinkedList<User>();
			for(int i = 1; i < data.length; i += 3) {
				users.add(new User(data[i], data[i+1], Integer.parseInt(data[i+2])));
			}
			return new RoomPacket(ID.JOIN_ROOM, users);
		case RUN_GAME:
			users = new LinkedList<User>();
			for(int i = 2; i < data.length; i += 3) {
				users.add(new User(data[i], data[i+1], Integer.parseInt(data[i+2])));
			}
			return new RunGamePacket(ID.RUN_GAME, users, data[1]);
		default:
			break;
		}
		
		return null;
	}
	
	public byte[] getData() {
		return null;
	}
	
	public int length() {
		return getData().length;
	}
}
