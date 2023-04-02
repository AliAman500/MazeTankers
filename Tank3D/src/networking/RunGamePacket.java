package networking;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jogamp.vecmath.Vector3f;

import enums.TankColor;

public class RunGamePacket extends Packet {

	public LinkedList<User> users = new LinkedList<User>();
	public String mazePNG;
	public Random random = new Random();
	public List<TankColor> tColors = new ArrayList<TankColor>();
	
	public LinkedList<Vector3f> spawnPoints = new LinkedList<Vector3f>();
	
	public RunGamePacket(ID packetID, LinkedList<User> users) {
		this.users = users;
		this.id = packetID;
		
		for(int i = 0; i < TankColor.values().length; i++) {
			tColors.add(TankColor.values()[i]);
		}
		
		int mazeIndex = random.nextInt(5) + 1;
		mazePNG = "res/mazes/maze-" + mazeIndex + ".png";
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(mazePNG));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i = 0; i < users.size(); i++) {
			User currentUser = users.get(i);
			currentUser.tColor = randomColor();
			tColors.remove(currentUser.tColor);
		}
		
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);

				int red = (pixel >> 16) & 255;
                int green = (pixel >> 8) & 255;
                int blue = pixel & 255;
                
                if(red == 128 && green == 128 && blue == 128) {
                	spawnPoints.add(new Vector3f(x * 4, -1.4f, y * 4));
                }
			}
		}
		
		for(int i = 0; i < users.size(); i++) {
			User currentUser = users.get(i);
			int rand = new Random().nextInt(spawnPoints.size());
			currentUser.position = spawnPoints.get(rand);
			spawnPoints.remove(rand);
		}
	}
	
	private TankColor randomColor() {
	    return tColors.get(new Random().nextInt(tColors.size()));
	}
	
	public RunGamePacket(ID packetID, LinkedList<User> users, String mazePNG) {
		this.users = users;
		this.id = packetID;
		this.mazePNG = mazePNG;
	}
	
	public byte[] getData() {
		String data = id.name() + " " + mazePNG;
		for(User user : users) {
			data += " " + user.username + " " + user.tColor + " " + user.ipAddress + " " + user.port + " " + user.position.x + " " + user.position.y + " " + user.position.z;
		}
		return data.getBytes();
	}
}
