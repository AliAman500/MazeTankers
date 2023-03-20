package components;

import ECS.Component;
import ECS.Entity;
import input.Keyboard;
import networking.PositionPacket;
import tools.Util;

public class NetworkTank extends Component {

	private Tank tank;
	private GunRecoil recoil;
	public PositionPacket posPacket;

	private float velocity = 0;
	private float speed = 0.17f;
	private float smoothness = 0.2f;

	public NetworkTank(Entity parent) {
		super(parent);
		tank = (Tank) parent.getComponent("Tank");
		recoil = (GunRecoil) parent.getComponent("GunRecoil");
	}

	public void update() {
		// control tank:
		if (posPacket != null) {
			if (posPacket.forwards)
				velocity = Util.lerp(velocity, speed, smoothness);
			else if (posPacket.backwards)
				velocity = Util.lerp(velocity, -speed, smoothness);
			else
				velocity = Util.lerp(velocity, 0, smoothness);

			tank.direction = posPacket.direction;
			tank.turretDirection = posPacket.turretDirection;
			tank.position.x += velocity * Math.cos(Math.toRadians(tank.direction + 90));
			tank.position.z -= velocity * Math.sin(Math.toRadians(tank.direction + 90));
		}
	}
}
