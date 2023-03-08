package networking;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Server {

	private DatagramSocket server;
	private static Font font = new Font("Ariel", Font.PLAIN, 13);
	
	private static JTextArea textArea;
	
	public Server() throws Exception {
		server = new DatagramSocket(9888);
		while(true) {
			byte[] dataReceived = new byte[1024];
			DatagramPacket dataPacket = new DatagramPacket(dataReceived, dataReceived.length);
			logln("Listening...");
			server.receive(dataPacket);
			Packet packet = Packet.parse(dataPacket);
			switch(packet.id) {
			case CONNECT:
				ConnectPacket connectPacket = (ConnectPacket) packet;
				logln("recieved connection request from client: " + connectPacket.username);
				sendData(connectPacket, dataPacket.getAddress(), dataPacket.getPort());
				logln("sent approval to client: " + connectPacket.username);
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
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		JFrame frame = new JFrame("Server");
		frame.setLayout(null);
		
		Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(font);
        textArea.setBounds(new Rectangle(0, 50, 786, 513));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(new Rectangle(0, 50, 786, 513));
        
        contentPane.add(scrollPane);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		new Server();
	}
	
	public static void logln(String s) {
		textArea.setText(textArea.getText() + "-> " + s + "\n");
	}
	
	public static void log(String s) {
		textArea.setText(textArea.getText() + s);
	}
}
