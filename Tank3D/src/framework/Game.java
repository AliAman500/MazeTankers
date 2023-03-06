package framework;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;

import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

public class Game extends JFrame {

	private static final long serialVersionUID = 1L;

	public Game() {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);

		SimpleUniverse su = new SimpleUniverse(canvas);
		Commons.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));

//		sceneBG.addChild(CommonsAA.key_Navigation(su));
//		sceneBG.compile();
//		su.addBranchGraph(sceneBG);

		setLayout(new BorderLayout());
		add("Center", canvas);
		this.setSize(800, 800);
		this.setTitle("Tank3D");
		this.setVisible(true);
		this.setSize(900, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Game();
	}
}
