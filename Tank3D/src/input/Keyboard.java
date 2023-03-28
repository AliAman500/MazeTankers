package input;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import entry.EndScreen;
import entry.Game;

public class Keyboard implements KeyListener {
	private static boolean up = false, down = false, left = false, right = false;
	private static boolean leftShiftDown = false;

	public static boolean isLeftShiftDown() {
		return leftShiftDown;
	}

	private static ArrayList<Integer> keysPressed = new ArrayList<Integer>();

	public void keyTyped(KeyEvent e) {

	}
	boolean fullScreen = false;
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_W) {
			Game.cc.activate();
			up = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			Game.cc.activate();
			down = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			Game.cc.activate();
			left = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			Game.cc.activate();
			right = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			Game.cc.activate();
			leftShiftDown = true;
		}
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (e.getKeyCode() == KeyEvent.VK_F) {
			if (!fullScreen) {
				fullScreen = true;
				gd.setFullScreenWindow(Game.frame);
			} else {
				fullScreen = false;
				gd.setFullScreenWindow(null);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			down = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			leftShiftDown = false;
	}

	public static void update() {
		keysPressed.clear();
	}

	public static boolean isUp() {
		return up;
	}

	public static boolean isDown() {
		return down;
	}

	public static boolean isLeft() {
		return left;
	}

	public static boolean isRight() {
		return right;
	}

	public static boolean isKeyPressed(int key) {
		return keysPressed.contains(key);
	}
}