package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import entry.Game;

public class Client implements Runnable {

	public DatagramSocket client;
	
	public InetAddress serverAddress;
	public int serverPort;
	public Game game;
	public String deviceName;
	
	public Client(Game game) throws Exception {
		this.game = game;
		client = new DatagramSocket();
		serverPort = 9888;
		serverAddress = InetAddress.getByName("192.168.2.35");
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
		
		while(true) {
			byte[] dataReceived = new byte[1024];
			DatagramPacket dataPacket = new DatagramPacket(dataReceived, dataReceived.length);
			System.out.println("listening...");
			try {
				client.receive(dataPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Packet packet = Packet.parse(dataPacket);
			switch(packet.id) {
			case CONNECT:
				System.out.println("recieved connection approval from server");
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
