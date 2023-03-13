package networking;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import networking.Packet.ID;

public class Server {

	private DatagramSocket server;
	private static String address = "";
	private static int port = 9888;
	private static Font font = new Font("Consolas", Font.PLAIN, 13);
	
	private static JTextArea textArea;
	private static JLabel label = new JLabel();
	private static JLabel loading = new JLabel();
	
	private static Thread loadingThread;
	
	public static LinkedList<Room> rooms = new LinkedList<Room>();
	
	public Server() throws Exception {
		server = new DatagramSocket(port);
		address = InetAddress.getLocalHost().getHostAddress();
		label.setText("Server listening on IP: " + address + " Port: " + port);
		logln("Listening...");
		while(true) {
			byte[] dataReceived = new byte[1024];
			DatagramPacket dataPacket = new DatagramPacket(dataReceived, dataReceived.length);
			server.receive(dataPacket);
			Packet packet = Packet.parse(dataPacket);
			switch(packet.id) {
			case PLAY_REQUEST:
				PlayReqPacket pPacket = (PlayReqPacket) packet;
				logln("recieved play request from client:- " + pPacket.username + " " + dataPacket.getAddress().getHostAddress() + " " + dataPacket.getPort());

				if(rooms.isEmpty()) {
					Room room = new Room();
					User user = new User(pPacket.username, dataPacket.getAddress().getHostAddress(), dataPacket.getPort(), null);
					room.addUser(user);
					RoomPacket rPacket = new RoomPacket(ID.CREATE_ROOM, room);
					sendData(rPacket, dataPacket.getAddress(), dataPacket.getPort());
					logln("sent room creation approval to client:- " + user);
					rooms.add(room);
				} else {
					Room room = rooms.get(0);
					User user = new User(pPacket.username, dataPacket.getAddress().getHostAddress(), dataPacket.getPort(), null);
					room.addUser(user);
					RoomPacket rPacket = new RoomPacket(ID.JOIN_ROOM, room);
					sendData(rPacket, user);
					logln("sent room join approval to client:- " + user);
					
					for(int i = 0; i < room.users.size(); i++) {
						User currentUser = room.users.get(i);
						if(!currentUser.equals(user)) {
							sendData(rPacket, currentUser);
							logln("sent room join approval to client:- " + currentUser);
						}
					}
				}
				
				break;
			case CREATE_ROOM:
				break;
			case JOIN_ROOM:
				break;
			case RUN_GAME:
				RunGamePacket runPacket = (RunGamePacket) packet;
				RunGamePacket runPacketUpdated = new RunGamePacket(runPacket.id, runPacket.users);
				for(int i = 0; i < runPacketUpdated.users.size(); i++) {
					User currentUser = runPacketUpdated.users.get(i);
					sendData(runPacketUpdated, currentUser);
					logln("maze and position generated, sent data to client:- " + currentUser);
				}
				break;
			case POSITION:
				PositionPacket posPacket = (PositionPacket) packet;
				
				for(int i = 0; i < posPacket.users.size(); i++) {
					User currentUser = posPacket.users.get(i);
					if(!currentUser.username.equals(posPacket.username)) {
						sendData(posPacket, currentUser);
					}
				}
				
				break;
			default:
				break;
			}
		}
	}
	
	public User findSender(DatagramPacket dataPacket, LinkedList<User> users) {
		for(int i = 0; i < users.size(); i++) {
			User currentUser = users.get(i);
			if(currentUser.ipAddress.equals(dataPacket.getAddress().getHostAddress()) && currentUser.port == dataPacket.getPort()) {
				return currentUser;
			}
		}
		
		return null;
	}
	
	public void sendData(Packet packet, InetAddress address, int port) {
		DatagramPacket dataPacket = new DatagramPacket(packet.getData(), packet.length(), address, port);
		try {
			server.send(dataPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendData(Packet packet, User user) {
		try {
			DatagramPacket dataPacket = new DatagramPacket(packet.getData(), packet.length(), InetAddress.getByName(user.ipAddress), user.port);
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
        
        label.setFont(new Font("Calibri", Font.PLAIN, 14));
        label.setBounds(new Rectangle(250, 10, 360, 32));

        loading.setFont(new Font("Consolas", Font.BOLD, 12));
        loading.setBounds(new Rectangle(365, 22, 360, 32));
        
        contentPane.add(label, "North");
        contentPane.add(loading, "North");
        
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
		
		loadingThread = new Thread(new Runnable() {
			public void run() {
				int waitTime = 300;
				while (true) {
					try {
						loading.setText(".");
						Thread.sleep(waitTime);
						loading.setText("..");
						Thread.sleep(waitTime);
						loading.setText("...");
						Thread.sleep(waitTime);
						loading.setText("....");
						Thread.sleep(waitTime);
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		loadingThread.start();
		
		new Server();
	}
	
	public static void logln(String s) {
		textArea.setText(textArea.getText() + "-> " + s + "\n");
	}
	
	public static void log(String s) {
		textArea.setText(textArea.getText() + s);
	}
}
