package input;

import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import org.jogamp.vecmath.Vector2f;

import entry.Game;

public class Mouse implements MouseListener {

	private static ArrayList<Integer> buttonsPressed = new ArrayList<Integer>();

	public static Vector2f position2D = new Vector2f();
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		buttonsPressed.add(e.getButton());
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
		buttonsPressed.clear();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public static boolean isButtonPressed(int button) {
		return buttonsPressed.contains(button);
	}
}