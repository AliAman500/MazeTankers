package entry;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Vector3f;

import ECS.ESystem;
import ECS.Entity;
import components.Tank;
import entities.Entities;
import input.Keyboard;
import input.Mouse;
import networking.Client;
import tools.Util;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;

	public static Client client;

	public static Thread clientThread;
	public Thread thread;

	public static Thread uiThread;

	private ESystem eSystem;

	public static SimpleUniverse simpleUniverse;
	public static Appearance COLOR_PALETTE;

	private Entity setupUserMaze(TransformGroup sceneTG) {
		Entity userTank = null;

		try {
			userTank = Util.setupMaze("res/mazes/maze-3.png", sceneTG, eSystem);
		} catch (Exception e) {
			System.err.print(e);
			System.exit(-1);
		}

		return userTank;
	}

	public BranchGroup createScene(SimpleUniverse simpleUniverse) {
		COLOR_PALETTE = Util.texturedAppearance("res/textures/color-palette.png");
		eSystem = new ESystem();

		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();
		BranchGroup staticLightGroup = new BranchGroup();

		Util.addDirectionalLight(staticLightGroup, new Vector3f(0.4f, -1, -1), Util.WHITE);

		Entity userTank = setupUserMaze(sceneTG);

		eSystem.addEntity(Entities.createCamera(simpleUniverse, (Tank) userTank.getComponent("Tank")));
		eSystem.addEntity(Entities.createFloor(sceneTG));

		sceneBG.addChild(staticLightGroup);
		sceneBG.addChild(sceneTG);

		return sceneBG;
	}

	public void gameLogic() {
		eSystem.update();

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
		Canvas3D canvas = new Canvas3D(config);
		canvas.addKeyListener(new Keyboard());
		canvas.addMouseListener(new Mouse());

		simpleUniverse = new SimpleUniverse(canvas);
		Util.enableAudio(simpleUniverse);

		BranchGroup sceneBG = createScene(simpleUniverse);
		sceneBG.addChild(Util.createBackgroundSound("res/audio/turkey-in-the-straw.wav"));

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
		client = new Client();
		clientThread = new Thread(client);
		clientThread.start();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		uiThread = new Thread(new Runnable() {
			public void run() {
				new Menu();
			}
		});
		uiThread.start();
	}
}