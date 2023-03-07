package entry;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;

import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

import networking.Client;
import networking.ConnectPacket;
import tools.Util;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;

	public Client client;
	public Thread thread;

	public Game() throws Exception {
		client = new Client(this);
		thread = new Thread(client);

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);

		SimpleUniverse su = new SimpleUniverse(canvas);
		Util.defineViewer(su, new Point3d(4.0d, 0.0d, 1.0d));

		setLayout(new BorderLayout());
		add("Center", canvas);

		this.setSize(1136, 640);
		this.setTitle("Maze Tankers");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		Game game = new Game();
		game.thread.start();
		game.client.sendData(new ConnectPacket("NewUser", game.client.clientAddress, game.client.clientPort));
		System.out.println("Just sent connection request to server (" + game.client.serverAddress + ":"
				+ game.client.serverPort + "). Waiting for approval...");

	}
}