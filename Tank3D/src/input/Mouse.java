package input;

import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import components.Tank;
import entry.Game;

public class Mouse implements MouseListener, MouseMotionListener {

	private static ArrayList<Integer> buttonsPressed = new ArrayList<Integer>();

	public static Vector2f position2D = new Vector2f();
	public static Vector3f position3D = new Vector3f(0, 14, 0);

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent event) {
		buttonsPressed.add(event.getButton());
	}

	public void mouseReleased(MouseEvent e) {

	}

	public static void update(Game game) {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

		Point frameLocation = game.getLocationOnScreen();
		Insets frameInsets = game.getInsets();
		frameLocation.x += frameInsets.left;
		frameLocation.y += frameInsets.top;

		position2D.x = (float) (mouseLocation.x - frameLocation.x);
		position2D.y = (float) (mouseLocation.y - frameLocation.y);

		int x = (int) position2D.x;
		int y = (int) position2D.y; // mouse coordinates
		Point3d point3d = new Point3d(), center = new Point3d();
		Game.canvas.getPixelLocationInImagePlate(x, y, point3d);// obtain AWT pixel in ImagePlate coordinates
		Game.canvas.getCenterEyeInImagePlate(center); // obtain eye's position in IP coordinates

		Transform3D transform3D = new Transform3D(); // matrix to relate ImagePlate coordinates~
		Game.canvas.getImagePlateToVworld(transform3D); // to Virtual World coordinates
		transform3D.transform(point3d); // transform 'point3d' with 'transform3D'
		transform3D.transform(center); // transform 'center' with 'transform3D'

		Vector3d mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();
		Game.picker.setShapeRay(point3d, mouseVec);

		if (Game.picker.pickClosest() != null) {
			PickResult pickResult = Game.picker.pickClosest();
			try {
				position3D.x = (float) pickResult.getIntersection(0).getPointCoordinates().x + 78;
				position3D.z = (float) pickResult.getIntersection(0).getPointCoordinates().z + 78;
			} catch (Exception e) {
				
			}
		}

		buttonsPressed.clear();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public static boolean isButtonPressed(int button) {
		return buttonsPressed.contains(button);
	}

	@Override
	public void mouseDragged(MouseEvent event) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}