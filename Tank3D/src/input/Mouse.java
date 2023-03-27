package input;

import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import entry.Game;

public class Mouse implements MouseListener, MouseWheelListener {
	private static float scroll = 0;
	private static boolean scrolled = false;

	public static float getScroll() {
		return scrolled ? scroll : 0;
	}

	private static ArrayList<Integer> buttonsPressed = new ArrayList<Integer>();
	private static Vector2f position2D = new Vector2f();
	public static Vector2f getPosition2D() {
		return position2D;
	}

	private static Vector2f delta = new Vector2f();
	private static Vector2f previous = new Vector2f();
	private static Vector3f position3D = new Vector3f();

	public static Vector3f getPosition3D() {
		return position3D;
	}

	public static Vector2f getDelta() {
		return delta;
	}

	private static boolean middleDown = false;
	
	public static boolean isMiddleDown() {
		return middleDown;
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
		int y = (int) position2D.y;
		Point3d point3d = new Point3d(), center = new Point3d();
		Game.canvas.getPixelLocationInImagePlate(x, y, point3d);
		Game.canvas.getCenterEyeInImagePlate(center);

		Transform3D transform3D = new Transform3D();
		Game.canvas.getImagePlateToVworld(transform3D);
		transform3D.transform(point3d);
		transform3D.transform(center);

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
		delta.sub(position2D, previous);
		previous.set(position2D);

		scrolled = false;
	}

	public void mouseClicked(MouseEvent e) {
			
	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			Game.cc.activate();
		buttonsPressed.add(e.getButton());

		if (e.getButton() == MouseEvent.BUTTON2)
			middleDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		middleDown = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public static boolean isButtonPressed(int button) {
		return buttonsPressed.contains(button);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		scrolled = true;
		scroll = e.getWheelRotation();
	}
}