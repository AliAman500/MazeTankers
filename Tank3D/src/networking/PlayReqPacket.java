package networking;

public class PlayReqPacket extends Packet {

	public String username;
	
	public PlayReqPacket(String username) {
		this.username = username;
		this.id = ID.PLAY_REQUEST;
	}
	
	public byte[] getData() {
		return ("PLAY_REQUEST " + username).getBytes();
	}
}
