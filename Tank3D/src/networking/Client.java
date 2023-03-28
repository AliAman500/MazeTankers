package networking;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;

import org.jogamp.vecmath.Vector3f;

import ECS.Entity;
import components.BoxCollider;
import components.GunRecoil;
import components.NetworkTank;
import components.Tank;
import entities.Entities;
import entry.Game;
import entry.Menu;
import networking.Packet.ID;

public class Client implements Runnable {

	public DatagramSocket client;

	public static String serverIP = "192.168.2.35";
	public InetAddress serverAddress;
	public int serverPort;
	public String deviceName;
	public String clientAddress;
	public int clientPort;

	public static JButton play = new JButton("Start Game!");
	
	public Client() throws Exception {
		client = new DatagramSocket();
		serverPort = 9888;
		deviceName = InetAddress.getLocalHost().getHostName();
		clientAddress = InetAddress.getLocalHost().getHostAddress();
		clientPort = client.getLocalPort();
	}

	public void sendData(Packet packet) {
		try {
			serverAddress = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DatagramPacket dataPacket = new DatagramPacket(packet.getData(), packet.length(), serverAddress, serverPort);
		try {
			client.send(dataPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] dataReceived = new byte[1024];
			DatagramPacket dataPacket = new DatagramPacket(dataReceived, dataReceived.length);
			try {
				client.receive(dataPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Packet packet = Packet.parse(dataPacket);
			switch (packet.id) {
			case CREATE_ROOM:
				RoomPacket cPacket = (RoomPacket) packet;
				Game.room = new Room(cPacket);
				Menu.cards.show(Menu.contentPane, "room");
				Menu.addUsername(Game.user.username);
				play = new JButton("Start Game!");
				play.setBounds(new Rectangle(1136 / 2 - 100, 440 + 50, 200, 46));
				play.setEnabled(false);
				play.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						RunGamePacket runPacket = new RunGamePacket(ID.RUN_GAME, Game.room.users);
						sendData(runPacket);
						play.setEnabled(false);
					}

				});

				Menu.roomPanel.add(play);
				Menu.roomPanel.setComponentZOrder(Menu.iconLabelr, 5);
				Menu.roomPanel.revalidate();
				Menu.roomPanel.repaint();
				break;
			case JOIN_ROOM:
				RoomPacket jPacket = (RoomPacket) packet;
				Game.room = new Room(jPacket);
				if(Game.room.users.size() >= 2) {
					play.setEnabled(true);
				}
				Menu.clearRows();
				for (int i = 0; i < jPacket.users.size(); i++) {
					Menu.addUsername(jPacket.users.get(i).username);
				}
				if(!Game.room.users.get(0).username.equals(Game.user.username))
					Menu.startingGameLabel.setText("Waiting for host");
				Menu.cards.show(Menu.contentPane, "room");
				Menu.roomPanel.repaint();
				break;
			case RUN_GAME:
				RunGamePacket rPacket = (RunGamePacket) packet;
				Game.mazePNG = rPacket.mazePNG;
				for (int i = 0; i < rPacket.users.size(); i++) {
					User currentUser = rPacket.users.get(i);
					if (Game.user.equals(currentUser)) {
						Game.user = currentUser;
						break;
					}
				}

				Game.room = new Room(rPacket);
				Menu.startingGameLabel.setForeground(new Color(50, 200, 200));
				Menu.startingGameLabel.setText("  Starting game...");
				Menu.roomPanel.revalidate();
				Menu.roomPanel.repaint();
				Menu.gameStarterThread.start();
				break;
			case POSITION:
				PositionPacket posPacket = (PositionPacket) packet;
				for (int i = 0; i < Game.eSystem.numEntities(); i++) {
					Entity e = Game.eSystem.getEntity(i);
					if(e != null) {
						Tank tank = (Tank) e.getComponent("Tank");
						if (tank != null) {
							if (tank.username.equals(posPacket.username)) {
								NetworkTank netTank = (NetworkTank) e.getComponent("NetworkTank");
								if (netTank != null)
									netTank.posPacket = posPacket;
							}
						}
					}
				}
				break;
			case BULLET:
				BulletPacket bulletPacket = (BulletPacket) packet;
				BoxCollider tankCollider = null;
				for (int i = 0; i < Game.eSystem.numEntities(); i++) {
					Entity e = Game.eSystem.getEntity(i);
					Tank tank = (Tank) e.getComponent("Tank");
					if (tank != null) {
						if (tank.username.equals(bulletPacket.username)) {
						tankCollider = (BoxCollider) e.getComponent("BoxCollider");
							GunRecoil recoil = (GunRecoil) e.getComponent("GunRecoil");
							recoil.playRecoil();
						}
					}
				}
				Game.eSystem.addEntity(Entities.createBullet(new Vector3f(bulletPacket.position),
						bulletPacket.direction, tankCollider, Game.sceneTG));
				break;
			default:
				break;
			}
		}
	}
}