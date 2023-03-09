package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener {
	private static boolean up = false, down = false, left = false, right = false;
	private static ArrayList<Integer> keysPressed = new ArrayList<Integer>();

    public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
		if(e.getKeyCode() == KeyEvent.VK_W) up = true;
		if(e.getKeyCode() == KeyEvent.VK_S) down = true;
		if(e.getKeyCode() == KeyEvent.VK_A) left = true;
		if(e.getKeyCode() == KeyEvent.VK_D) right = true;
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) up = false;
		if(e.getKeyCode() == KeyEvent.VK_S) down = false;
		if(e.getKeyCode() == KeyEvent.VK_A) left = false;
		if(e.getKeyCode() == KeyEvent.VK_D) right = false;
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