package entities;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3f;

import ECS.Entity;
import components.Audio;
import components.BackgroundEntity;
import components.Barrels;
import components.Block;
import components.BoxCollider;
import components.Bullet;
import components.Camera;
import components.CameraController;
import components.Firefly;
import components.Floor;
import components.Forest;
import components.GunRecoil;
import components.NetworkTank;
import components.PlantPot;
import components.PlayerController;
import components.Skull;
import components.Skybox;
import components.Tank;
import components.Torch;
import entry.Game;
import enums.TankColor;
import enums.TorchDirection;

public class Entities {
	public static Entity createUserTank(TransformGroup sceneTG, Vector3f position, TankColor color) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Audio("res/audio/tank-related/artillery.wav", 10, -1, new Point3f(), 0, e));
		e.addComponent(new BoxCollider(new Vector2f(3, 4), e));
		e.addComponent(new Tank(Game.user.username, position, color, e));
		e.addComponent(new GunRecoil(e));
		e.addComponent(new PlayerController(e));
		return e;
	}

	public static Entity createBarrels(TransformGroup sceneTG, Vector3f position) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Barrels(position, e));
		return e;
	}

	public static Entity createBackgroundEntity(TransformGroup sceneTG, Entity camera) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new BackgroundEntity(camera, e));
		Audio a1 = (Audio) e.addComponent("audio1", new Audio("res/audio/backgrounds/ambience.wav", 0.08f, -1, new Point3f(), 0, e));
		Audio a2 = (Audio) e.addComponent("audio2", new Audio("res/audio/backgrounds/crickets.wav", 0.15f, -1, new Point3f(), 0, e));
		
		a1.play();
		a2.play();
		return e;
	}

	public static Entity createFirefly(Vector3f position, TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Firefly(position, e));
		return e;
	}

	public static Entity createBullet(Vector3f position, float direction, BoxCollider shooter, TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Audio("res/audio/maze/wall-hit.wav", 10, -1, new Point3f(), 0.03f, e));
		e.addComponent(new BoxCollider(new Vector2f(0.35f, 0.35f), e));
		e.addComponent(new Bullet(e, position, shooter, direction));
		return e;
	}

	public static Entity createSkybox(Entity camera, TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Skybox(camera, e));
		return e;
	}

	public static Entity createPlantPot(TransformGroup sceneTG, Vector3f position) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new PlantPot(position, e));
		return e;
	}

	public static Entity createSkull(TransformGroup sceneTG, Vector3f position) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Skull(position, e));
		return e;
	}

	public static Entity createNetworkTank(TransformGroup sceneTG, Vector3f position, String username,
			TankColor color) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Audio("res/audio/tank-related/artillery.wav", 10, -1, new Point3f(), 0, e));
		e.addComponent(new BoxCollider(new Vector2f(3, 4), e));
		e.addComponent(new Tank(username, position, color, e));
		e.addComponent(new GunRecoil(e));
		e.addComponent(new NetworkTank(e));
		return e;
	}

	public static Entity createForest(TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Forest(new Vector3f(-70, -4.5f, -70), e));
		return e;
	}

	public static Entity createBlock(TransformGroup sceneTG, Vector3f position) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Block(position, e));
		e.addComponent(new BoxCollider(new Vector2f(4, 4), e));
		return e;
	}

	public static Entity createTorch(TransformGroup sceneTG, Vector3f position, TorchDirection direction) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Torch(position, direction, e));
		return e;
	}

	public static Entity createCamera(SimpleUniverse simpleUniverse, Tank userTank) {
		Entity e = new Entity();
		e.addComponent(new Camera(new Vector3f(0, 50, 0), new Vector3f(), 90, simpleUniverse, e));
		e.addComponent(new CameraController(userTank, e));
		return e;
	}

	public static Entity createFloor(TransformGroup sceneTG) {
		Entity e = new Entity(sceneTG);
		e.addComponent(new Floor(e));
		return e;
	}
}
