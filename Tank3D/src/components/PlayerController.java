package components;

import java.awt.event.MouseEvent;

import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entities.Entities;
import entry.Game;
import input.Keyboard;
import input.Mouse;
import networking.BulletPacket;
import networking.PositionPacket;
import tools.Util;

public class PlayerController extends Component {
	private Tank tank;
	private GunRecoil recoil;

	private float velocity = 0;
	private float speed = 0.17f;
	private float turnSmoothness = 0.2f;
	private float smoothness = 0.2f;
	
	private float rotationVelocity = 0;
	private float rotationSpeed = 5;

	private boolean forwards = false;
	private boolean backwards = false;
	
	public PlayerController(Entity parent) {
		super(parent);
		this.tank = (Tank) parent.getComponent("Tank");
		this.recoil = (GunRecoil) parent.getComponent("GunRecoil");
	}

	public void update() {
		tank.position.x += velocity * Math.cos(Math.toRadians(tank.direction + 90));
		tank.position.z -= velocity * Math.sin(Math.toRadians(tank.direction + 90));
		
		tank.turretDirection = -((((float) (Math.atan2((tank.position.z -0.356371f) - Mouse.position3D.z, (tank.position.x - 0.27f) - Mouse.position3D.x) * 180 / Math.PI)) - 90) + tank.direction);
		
		forwards = Keyboard.isUp();
		backwards = Keyboard.isDown();
		
		if (Keyboard.isUp())
			velocity = Util.lerp(velocity, speed, smoothness);
		else if (Keyboard.isDown())
			velocity = Util.lerp(velocity, -speed, smoothness);
		else
			velocity = Util.lerp(velocity, 0, smoothness);

		if (Keyboard.isRight())
			rotationVelocity = Util.lerp(rotationVelocity, -rotationSpeed, turnSmoothness);
		else
			rotationVelocity = Util.lerp(rotationVelocity, 0, turnSmoothness);
		
		tank.direction += rotationVelocity;

		if (Keyboard.isLeft())
			rotationVelocity = Util.lerp(rotationVelocity, rotationSpeed, turnSmoothness);
		else {
			rotationVelocity = Util.lerp(rotationVelocity, 0, turnSmoothness);
		}

		tank.direction += rotationVelocity;

		if (Mouse.isButtonPressed(MouseEvent.BUTTON1) && !recoil.playing) {
			recoil.playRecoil();
			if(Game.room != null) {
				BulletPacket bulletPack = new BulletPacket(Game.user.username, new Vector3f(tank.position), new Vector3f(Mouse.position3D), Game.room.users);
				Game.client.sendData(bulletPack);
			}
		}
		
		if(Game.room != null) {
			PositionPacket posPacket = new PositionPacket(Game.user.username, forwards, backwards, tank.direction, tank.turretDirection, Game.room);
			Game.client.sendData(posPacket);
		}
	}
}