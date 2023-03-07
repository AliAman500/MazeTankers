package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JFrame;

public class Server {

	private DatagramSocket server;
	
	public Server() throws Exception {
		server = new DatagramSocket(9876, InetAddress.getLocalHost());
		
		byte[] dataReceived = new byte[1024];
		
		while(true) {
			DatagramPacket dataPacket = new DatagramPacket(dataReceived, dataReceived.length);
			server.receive(dataPacket);
			Packet packet = Packet.parse(dataPacket);
			
			switch(packet.id) {
			case CONNECT:
				ConnectPacket connectPacket = (ConnectPacket) packet;
				System.out.println("Received connection request from client " + connectPacket.username + ".");
				sendData(connectPacket, connectPacket.address, connectPacket.port);
				System.out.println("Sent connection approval to client " + connectPacket.username + ".");
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
	
	public void sendData(Packet packet, InetAddress address, int port) {
		DatagramPacket dataPacket = new DatagramPacket(packet.getData(), packet.length(), address, port);
		try {
			server.send(dataPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(600, 400);
		frame.setVisible(true);
		new Server();
	}
	
}
