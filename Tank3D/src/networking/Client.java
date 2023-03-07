package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import entry.Game;

public class Client implements Runnable {

	private DatagramSocket client;
	
	public InetAddress serverAddress;
	public int serverPort;
	public InetAddress clientAddress;
	public int clientPort;
	public Game game;
	public String deviceName;
	
	public Client(Game game) throws Exception {
		this.game = game;
		client = new DatagramSocket(9877);
		clientAddress = InetAddress.getByName("10.72.93.113");
		clientPort = client.getPort();
		serverPort = 9876;
		serverAddress = InetAddress.getByName("10.72.93.113");
		deviceName = InetAddress.getLocalHost().getHostName();
	}
	
	public void sendData(Packet packet) {
		DatagramPacket dataPacket = new DatagramPacket(packet.getData(), packet.length(), serverAddress, serverPort);
		try {
			client.send(dataPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		byte[] dataReceived = new byte[1024];
		
		while(true) {
			DatagramPacket dataPacket = new DatagramPacket(dataReceived, dataReceived.length);
			try {
				client.receive(dataPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Packet packet = Packet.parse(dataPacket);
			
			switch(packet.id) {
			case CONNECT:
				System.out.println("Received approval for connection from server (" + packet.address  + ":" + packet.port + ")");
				break;
			case CREATE_ROOM:
				break;
			case JOIN_ROOM:
				break;
			default:
				break;
			}
		}
	}
	
}
