package networking;

import java.net.InetAddress;

public class ConnectPacket extends Packet {

	public String username;
	
	public ConnectPacket(String username, InetAddress address, int port) {
		this.username = username;
		this.id = ID.CONNECT;
		this.address = address;
		this.port = port;
	}
	
	public byte[] getData() {
		return ("CONNECT " + username).getBytes();
	}
}
