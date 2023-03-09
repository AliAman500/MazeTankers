package entry;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.util.LinkedList;

import javax.swing.JFrame;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Vector3f;

import ECS.Entity;
import components.Camera;
import components.CameraController;
import components.PlayerController;
import components.Tank;
import enums.TankColor;
import input.Keyboard;
import input.Mouse;
import networking.Client;
import networking.ConnectPacket;
import tools.Util;

public class Game extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public Client client;
	public Thread clientThread;
	public Thread thread;
	public static SimpleUniverse simpleUniverse;
	
	public static Appearance COLOR_PALETTE;
	private LinkedList<Entity> entities;

	private Tank setupUserTank(TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);

		Tank tank = (Tank) e.addComponent(new Tank(new Vector3f(0, 0, 0), TankColor.RED, e));
		
		e.addComponent(new PlayerController(e));

		entities.add(e);
		return tank;
	}

	private void setupUserMaze(TransformGroup sceneTG) {
		try {
            sceneTG.addChild(Util.createMazeFromImage("res/mazes/maze-big.png"));
        }catch(Exception e) {
            System.err.print(e);
			System.exit(-1);
        }
	}
	
	private void setupUserCamera(Tank tank, SimpleUniverse simpleUniverse) {
		Entity e = new Entity();
			
		e.addComponent(new Camera(new Vector3f(0, 35, 35), new Vector3f(), 45, simpleUniverse, e));
		e.addComponent(new CameraController(tank, e));

		entities.add(e);
	}

	public BranchGroup createScene(SimpleUniverse simpleUniverse) {
		COLOR_PALETTE = Util.texturedAppearance("res/textures/color-palette.png");

		entities = new LinkedList<Entity>();
		
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();
		BranchGroup staticLightGroup = new BranchGroup();

		Util.addDirectionalLight(staticLightGroup, new Vector3f(0.4f, -1, -1), Util.WHITE);
		Tank tank = setupUserTank(sceneTG);
		setupUserCamera(tank, simpleUniverse);
		setupUserMaze(sceneTG);
		sceneBG.addChild(staticLightGroup);
		sceneBG.addChild(sceneTG);

		return (sceneBG);
	}

	public void gameLogic() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update(entities);

		Mouse.update(this);
		Keyboard.update();
	}

	public Game() throws Exception {
		client = new Client(this);
		clientThread = new Thread(client);

		thread = new Thread(new Runnable() {
			public void run() {
				long lastTime = System.nanoTime();
				double amountOfTicks = 60.0;
				double ns = 1000000000 / amountOfTicks;
				double delta = 0;
				long timer = System.currentTimeMillis();
				while (true) {
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
					while (delta >= 1) {
						gameLogic();
						delta--;
					}

					if (System.currentTimeMillis() - timer > 1000) {
						timer += 1000;
					}
				}
			}
		});

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		canvas.addKeyListener(new Keyboard());
		canvas.addMouseListener(new Mouse());

		simpleUniverse = new SimpleUniverse(canvas);
		Util.enableAudio(simpleUniverse);
		
		BranchGroup sceneBG = createScene(simpleUniverse);
		sceneBG.addChild(Util.bkgdSound("audio"));
		
		sceneBG.compile();
		simpleUniverse.addBranchGraph(sceneBG);

		setLayout(new BorderLayout());
		add("Center", canvas);

		this.setSize(1136, 640);
		this.setTitle("Maze Tankers");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	public static void main(String[] args) throws Exception {
		Game game = new Game();
		game.thread.start();
		game.clientThread.start();
		game.client.sendData(new ConnectPacket(game.client.deviceName));
	}
}