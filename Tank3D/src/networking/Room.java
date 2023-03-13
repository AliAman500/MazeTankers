package networking;

import java.util.LinkedList;

public class Room {

	public LinkedList<User> users = new LinkedList<User>();
	
	public Room() {
		
	}
	
	public Room(RoomPacket rPacket) {
		for(User user : rPacket.users) {
			users.add(user);
		}
	}
	
	public Room(RunGamePacket rPacket) {
		for(User user : rPacket.users) {
			users.add(user);
		}
	}
	
	public boolean has(User user) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).username.equals(user.username) && users.get(i).ipAddress.equals(user.ipAddress) && users.get(i).port == user.port) {
				return true;
			}
		}
		return false;
	}
	
	public void addUser(User user) {
		if(!users.contains(user))
			users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
}

