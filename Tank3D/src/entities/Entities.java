package entities;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Vector3f;

import ECS.Entity;
import components.Block;
import components.Camera;
import components.CameraController;
import components.Floor;
import components.GunRecoil;
import components.NetworkTank;
import components.PlayerController;
import components.Tank;
import enums.TankColor;

public class Entities {
    public static Entity createUserTank(TransformGroup sceneTG, Vector3f position) {
        Entity e = new Entity(sceneTG);
		e.addComponent(new Tank(position, TankColor.RED, e));
		e.addComponent(new PlayerController(e));
		e.addComponent(new GunRecoil(e));
        return e;
    }

    public static Entity createNetworkTank(TransformGroup sceneTG, Vector3f position) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Tank(position, TankColor.BLUE, e));
        e.addComponent(new NetworkTank(e));
        e.addComponent(new GunRecoil(e));
        return e;
    }
    
    public static Entity createBlock(TransformGroup sceneTG, Vector3f position) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Block(position, e));
        return e;
    }

    public static Entity createCamera(SimpleUniverse simpleUniverse, Tank userTank) {
        Entity e = new Entity();
		e.addComponent(new Camera(new Vector3f(0, 35, 35), new Vector3f(), 45, simpleUniverse, e));
		e.addComponent(new CameraController(userTank, e));
		return e;
    }

    public static Entity createFloor(TransformGroup sceneTG) {
        Entity e = new Entity(sceneTG);
        e.addComponent(new Floor(e));
        return e;
    }
}
