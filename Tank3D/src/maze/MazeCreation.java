package maze;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.Vector3f;

import tools.Util;

public class MazeCreation {

	public static Node createMazeFromImage(String fileName) throws Exception {
		TransformGroup mazeTG = new TransformGroup();
		BufferedImage img = ImageIO.read(new File("res/textures/" + fileName + ".png"));
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);
				if (pixel != -1) {
					Transform3D transform = new Transform3D();
					transform.setTranslation(new Vector3f(x * 1, 0, y * 1));
					TransformGroup boxTG = new TransformGroup(transform);
					if (pixel == -16777216) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.ORANGE)));
					} else if (pixel == -65536) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.RED)));
					} else if (pixel == -16711936) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.GREEN)));
					} else if (pixel == -16776961) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.BLUE)));
					}
					mazeTG.addChild(boxTG);
				}
			}
		}
		Transform3D scaler = new Transform3D();
		scaler.setScale(2);
		mazeTG.setTransform(scaler);
		return mazeTG;
	}
	
	public static String pickRandomMaze() {
		Random rand = new Random();
		int result = rand.nextInt(3-1) + 1;
		if(result == 1) {
			return "MazeDiagram1";
		}else if(result == 2) {
			return "MazeDiagram2";
		}
		return "MazeDiagram";
	}
}
