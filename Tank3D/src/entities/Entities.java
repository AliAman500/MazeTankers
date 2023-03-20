package entities;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Vector3f;

import ECS.Entity;
import components.Block;
import components.Bullet;
import components.Camera;
import components.CameraController;
import components.Floor;
import components.GunRecoil;
import components.NetworkTank;
import components.PlayerController;
import components.Tank;
import components.Torch;
import entry.Game;
import enums.TankColor;
import enums.TorchDirection;

public class Entities {
    public static Entity createUserTank(TransformGroup sceneTG, Vector3f position, TankColor color) {
        Entity e = new Entity(sceneTG);
		e.addComponent(new Tank(Game.user.username, position, color, e));
        e.addComponent(new GunRecoil(e));
		e.addComponent(new PlayerController(e));
        return e;
    }

    public static Entity createNetworkTank(TransformGroup sceneTG, Vector3f position, String username, TankColor color) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Tank(username, position, color, e));
        e.addComponent(new GunRecoil(e));
        e.addComponent(new NetworkTank(e));
        return e;
    }
	
	public static Entity createBullet(Vector3f position, Vector3f target, TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);
        e.addComponent(new Bullet(e, position, target));
        return e;
	}
    
    public static Entity createBlock(TransformGroup sceneTG, Vector3f position) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Block(position, e));
        return e;
    }

    public static Entity createTorch(TransformGroup sceneTG, Vector3f position, TorchDirection direction) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Torch(position, direction, e));
        return e;
    }

    public static Entity createCamera(SimpleUniverse simpleUniverse, Tank userTank) {
        Entity e = new Entity();
		e.addComponent(new Camera(new Vector3f(0, 80, 35), new Vector3f(), 45, simpleUniverse, e));
		e.addComponent(new CameraController(userTank, e));
		return e;
    }

    public static Entity createFloor(TransformGroup sceneTG) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Floor(e));
        return e;
    }
}
