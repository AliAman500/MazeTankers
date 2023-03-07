package networking;

public class ConnectPacket extends Packet {

	public String username;
	
	public ConnectPacket(String username) {
		this.username = username;
		this.id = ID.CONNECT;
	}
	
	public byte[] getData() {
		return ("CONNECT " + username).getBytes();
	}
}
