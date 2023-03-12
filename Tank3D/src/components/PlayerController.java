package components;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;
import input.Keyboard;
import tools.Util;

public class PlayerController extends Component {
	private Tank tank;

	private float velocity = 0;
	private float speed = 0.17f;
	private float turnSmoothness = 0.2f;
	private float smoothness = 0.2f;
	
	private float rotationVelocity = 0;
	private float rotationSpeed = 5;

	public PlayerController(Entity parent) {
		super(parent);
		this.tank = (Tank) parent.getComponent("Tank");
	}

	public void update(ESystem eSystem) {
		tank.position.x += velocity * Math.cos(Math.toRadians(tank.direction + 90));
		tank.position.z -= velocity * Math.sin(Math.toRadians(tank.direction + 90));

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

//		tank.turretDirection -= 2;
//		tank.gunOffset = (float) (0.22f * Math.sin(tank.direction * 0.2f));
	}
}