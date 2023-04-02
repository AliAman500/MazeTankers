package entry;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.JFrame;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.ESystem;
import ECS.Entity;
import components.CameraController;
import components.Tank;
import entities.Entities;
import input.Keyboard;
import input.Mouse;
import networking.Client;
import networking.Room;
import networking.User;
import tools.TextureData;
import tools.Util;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;

	public static Client client;
	public static LinkedList<String> leaderBoard = new LinkedList<String>();
	
	public static Thread clientThread;
	public Thread thread;

	public static Thread uiThread;

	public static ESystem eSystem = new ESystem();
	public static Canvas3D canvas;
	public static PickTool picker;
	public static CameraController cc;

	public static SimpleUniverse simpleUniverse;
	public static TextureData COLOR_PALETTE;
	public static TransformGroup sceneTG;
	public static BranchGroup sceneBG;
	
	public static Room room;
	public static User user = new User("SinglePlayer", null, null, 0, null);
	public static String mazePNG = "res/mazes/maze-" + 4 + ".png";
	public static Entity userTank;

	public static JFrame frame;
	
	private Entity setupUserMaze(TransformGroup sceneTG) {
		Entity userTank = null;

		try {
			userTank = Util.setupMaze(mazePNG, sceneTG, eSystem);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return userTank;
	}

	public BranchGroup createScene(SimpleUniverse simpleUniverse) {
		COLOR_PALETTE = Util.loadTexture("res/textures/color-palette.png");

		sceneBG = new BranchGroup();
		sceneTG = new TransformGroup();
		sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		sceneTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		BranchGroup staticLightGroup = new BranchGroup();
		
		Util.addDirectionalLight(staticLightGroup, new Vector3f(-1, -1, 0.4f), new Color3f(0.7f, 0.7f, 0.7f));

		userTank = setupUserMaze(sceneTG);
		
		Entity c = eSystem.addEntity(Entities.createCamera(simpleUniverse, (Tank) userTank.getComponent("Tank")));
		eSystem.addEntity(Entities.createBackgroundEntity(sceneTG, c));
		cc = (CameraController) c.getComponent("CameraController");
		eSystem.addEntity(Entities.createSkybox(c, sceneTG));
		eSystem.addEntity(Entities.createFloor(sceneTG));
		eSystem.addEntity(Entities.createForest(sceneTG));

		sceneBG.addChild(staticLightGroup);
		sceneBG.addChild(sceneTG);

		return sceneBG;
	}
	boolean gameEnded = false;
	public void gameLogic() {
		eSystem.update();
		if(room != null) {
			int numTanks = 0;
			for(int i = 0; i < eSystem.numEntities(); i++) {
				Entity e = eSystem.getEntity(i);
				Tank tank = (Tank) e.getComponent("Tank");
				if(tank != null) {
					numTanks++;
				}
			}
			
			if(numTanks <= 1 && !gameEnded) {
				for(int i = 0; i < eSystem.numEntities(); i++) {
					Entity e = eSystem.getEntity(i);
					Tank tank = (Tank) e.getComponent("Tank");
					if(tank != null) {
						leaderBoard.addFirst(tank.username);
						break;
					}
				}
				new EndScreen(leaderBoard);
				gameEnded = true;
				frame.dispose();
			}
		}
		Mouse.update(this);
		Keyboard.update();

	}

	public Game() throws Exception {
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
		canvas = new Canvas3D(config);
		canvas.addKeyListener(new Keyboard());
		Mouse m = new Mouse();
		canvas.addMouseListener(m);
		canvas.addMouseWheelListener(m);

		simpleUniverse = new SimpleUniverse(canvas);
		Util.enableAudio(simpleUniverse);

		BranchGroup sceneBG = createScene(simpleUniverse);
		BranchGroup floorBg = Util.createInvisibleFloor();
		
		sceneBG.addChild(floorBg);

		picker = new PickTool(floorBg);
		picker.setMode(PickTool.GEOMETRY);
		sceneBG.compile();
		simpleUniverse.addBranchGraph(sceneBG);

		setLayout(new BorderLayout());
		add("Center", canvas);
		
		Image windowIcon = Toolkit.getDefaultToolkit().getImage("res/textures/icon.png");
        this.setIconImage(windowIcon);
		this.setSize(1136, 640);
		this.setTitle("Maze Tankers");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		frame = this;
	}

	public static void startGame() {
		Game game = null;
		try {
			game = new Game();
		} catch (Exception e) {
			e.printStackTrace();
		}
		game.thread.start();
	}

	public static void main(String[] args) throws Exception {
		Menu.changeLookAndFeel();
		client = new Client();
		clientThread = new Thread(client);
		clientThread.start();

		uiThread = new Thread(new Runnable() {
			public void run() {
				new Menu();
			}
		});
		uiThread.start();
	}
}