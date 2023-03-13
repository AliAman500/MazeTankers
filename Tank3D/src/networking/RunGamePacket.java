package networking;

import java.util.LinkedList;
import java.util.Random;

public class RunGamePacket extends Packet {

	public LinkedList<User> users = new LinkedList<User>();
	public String mazePNG;
	public Random random = new Random();
	
	public RunGamePacket(ID packetID, LinkedList<User> users) {
		this.users = users;
		this.id = packetID;
		
		int mazeIndex = random.nextInt(3) + 1;
		mazePNG = "res/mazes/maze-" + 3 + ".png";
	}
	
	public RunGamePacket(ID packetID, LinkedList<User> users, String mazePNG) {
		this.users = users;
		this.id = packetID;
		this.mazePNG = mazePNG;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + mazePNG;
		for(User user : users) {
			data += " " + user.username + " " + user.ipAddress + " " + user.port;
		}
		return data.getBytes();
	}
}
