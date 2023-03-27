package components;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import networking.PositionPacket;
import tools.Util;

public class NetworkTank extends Component {

	private Tank tank;
	public PositionPacket posPacket;

	private float velocity = 0;
	private float speed = 0.17f;
	private float smoothness = 0.2f;

	private BoxCollider boxCollider;


	public NetworkTank(Entity parent) {
		super(parent);
		tank = (Tank) parent.getComponent("Tank");
		boxCollider = (BoxCollider) parent.getComponent("BoxCollider");
	}

	public void update() {
		// control tank:
		if (!tank.die) {
			if (posPacket != null) {
				if (posPacket.forwards)
					velocity = Util.lerp(velocity, speed, smoothness);
				else if (posPacket.backwards)
					velocity = Util.lerp(velocity, -speed, smoothness);
				else
					velocity = Util.lerp(velocity, 0, smoothness);

				for (int i = 0; i < Game.eSystem.numEntities(); i++) {
					Entity e = Game.eSystem.getEntity(i);
					Block b = (Block) e.getComponent("Block");
					BoxCollider bc = (BoxCollider) e.getComponent("BoxCollider");

					if (b != null) {
						if (BoxCollider.collision(boxCollider.getBoundsLeft(tank.position), bc.getBounds(b.position))) {
							tank.position.x = b.position.x + 3.8f;
						}

						if (BoxCollider.collision(boxCollider.getBoundsRight(tank.position),
								bc.getBounds(b.position))) {
							tank.position.x = b.position.x - 3.8f;
						}

						if (BoxCollider.collision(boxCollider.getBoundsBottom(tank.position),
								bc.getBounds(b.position))) {
							tank.position.z = b.position.z - 4.4f;
						}

						if (BoxCollider.collision(boxCollider.getBoundsTop(tank.position), bc.getBounds(b.position))) {
							tank.position.z = b.position.z + 4.4f;
						}
					}
				}

				tank.direction = posPacket.direction;
				tank.turretDirection = posPacket.turretDirection;
				tank.position.x += velocity * Math.cos(Math.toRadians(tank.direction + 90));
				tank.position.z -= velocity * Math.sin(Math.toRadians(tank.direction + 90));
			}
		}
	}
}