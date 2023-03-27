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
	private BoxCollider boxCollider;

	private float velocity = 0;
	private float speed = 0.17f;
	private float turnSmoothness = 0.2f;
	private float smoothness = 0.2f;
	private float velX = 0;
	private float velZ = 0;

	private float rotationVelocity = 0;
	private float rotationSpeed = 5;

	public int bulletCount = 5;
	public int bulletCooldown = 200;
	private int bCounter = 0;
	public boolean startCooldown = false;
	private boolean forwards = false;
	private boolean backwards = false;
	
	public PlayerController(Entity parent) {
		super(parent);
		this.tank = (Tank) parent.getComponent("Tank");
		this.recoil = (GunRecoil) parent.getComponent("GunRecoil");
		this.boxCollider = (BoxCollider) parent.getComponent("BoxCollider");
	}

	public void update() {
		if (!tank.die) {
			forwards = Keyboard.isUp();
			backwards = Keyboard.isDown();
			tank.turretDirection = -((((float) (Math.atan2((tank.position.z) - Mouse.getPosition3D().z,
					(tank.position.x) - Mouse.getPosition3D().x) * 180 / Math.PI)) - 90) + tank.direction);

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

			for (int i = 0; i < Game.eSystem.numEntities(); i++) {
				Entity e = Game.eSystem.getEntity(i);
				Block b = (Block) e.getComponent("Block");
				BoxCollider bc = (BoxCollider) e.getComponent("BoxCollider");

				if (b != null) {
					if (BoxCollider.collision(boxCollider.getBoundsLeft(tank.position), bc.getBounds(b.position))) {
						tank.position.x = b.position.x + 3.8f;
					}

					if (BoxCollider.collision(boxCollider.getBoundsRight(tank.position), bc.getBounds(b.position))) {
						tank.position.x = b.position.x - 3.8f;
					}

					if (BoxCollider.collision(boxCollider.getBoundsBottom(tank.position), bc.getBounds(b.position))) {
						tank.position.z = b.position.z - 4.4f;
					}

					if (BoxCollider.collision(boxCollider.getBoundsTop(tank.position), bc.getBounds(b.position))) {
						tank.position.z = b.position.z + 4.4f;
					}
				}
			}
			velX = (float) (velocity * Math.cos(Math.toRadians(tank.direction + 90)));
			velZ = -(float) (velocity * Math.sin(Math.toRadians(tank.direction + 90)));

			tank.position.x += velX;
			tank.position.z += velZ;

			if (Mouse.isButtonPressed(MouseEvent.BUTTON1) && !recoil.isPlaying()) {
				recoil.playRecoil();
				if (Game.room != null && bulletCount > 0) {
					BulletPacket bulletPack = new BulletPacket(Game.user.username, new Vector3f(tank.getGunWorld()),
							tank.turretDirection + tank.direction, Game.room.users);
					Game.client.sendData(bulletPack);
					bulletCount--;
				} else if (Game.room == null) {
					Game.eSystem.addEntity(Entities.createBullet(new Vector3f(tank.getGunWorld()),
							tank.turretDirection + tank.direction, boxCollider, Game.sceneTG));
				}
			}

			if (bulletCount == 0) {
				startCooldown = true;
			}

			if (startCooldown) {
				bCounter++;
				if (bCounter >= bulletCooldown) {
					bulletCount = 5;
					bCounter = 0;
					startCooldown = false;
				}
			}

			if (Game.room != null) {
				PositionPacket posPacket = new PositionPacket(Game.user.username, forwards, backwards, tank.direction, tank.turretDirection, Game.room);
				Game.client.sendData(posPacket);
			}
		}
	}
}