package components;

import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import input.Keyboard;
import input.Mouse;
import tools.Util;

public class CameraController extends Component {
	private Camera camera;
	public Tank target;

	private float distance = 5;
	private float panSpeed = 0.001f;
	private float smoothness = 0.2f;
	private float lx;
	private float ly;
	private float ldistance;
	private float moveSpeed = 5.0f;
	private Vector3f lOffset;
	private Vector2f mouseOffset;

	private Vector3f offset;
	private Vector3f rotOffset;

	private boolean activated = false;

	public static boolean playerDied = false;

	public CameraController(Tank target, Entity parent) {
		super(parent);
		camera = (Camera) parent.getComponent("Camera");

		this.target = target;

		offset = new Vector3f(target.position);
		rotOffset = new Vector3f();
		mouseOffset = new Vector2f(2, 0);

		lx = rotOffset.x;
		ly = rotOffset.y;
		ldistance = distance;
		lOffset = new Vector3f(offset);
	}

	private boolean firstActivate = false;

	public void setDefaultView() {
		mouseOffset.x = -74;
		mouseOffset.y = 0;
		distance = 48;
	}

	public void activate() {
		if (!firstActivate) {
			activated = true;
			setDefaultView();

			firstActivate = true;
		}
	}

	public void update() {
		rotOffset.x = (float) Math.toRadians(rotOffset.x - mouseOffset.x);
		rotOffset.y = (float) Math.toRadians(rotOffset.y - mouseOffset.y);
		rotOffset.z = (float) Math.toRadians(rotOffset.z);

		if (playerDied) {
			offset.set(new Vector3f(80, -1.4f, 80));
			if (Mouse.isMiddleDown()) {
				if (Keyboard.isLeftShiftDown()) {
					Vector3f right = new Vector3f();
					right.x = (float) Math.cos(Math.toRadians(camera.rotation.y));
					right.z = (float) -Math.sin(Math.toRadians(camera.rotation.y));

					Vector3f up = new Vector3f();
					up.x = (float) (Math.sin(Math.toRadians(camera.rotation.x))
							* Math.sin(Math.toRadians(camera.rotation.y)));
					up.y = (float) Math.cos(Math.toRadians(camera.rotation.x));
					up.z = (float) (Math.sin(Math.toRadians(camera.rotation.x))
							* Math.cos(Math.toRadians(camera.rotation.y)));

					offset.x -= right.x * (float) Mouse.getDelta().x * panSpeed * distance;
					offset.y -= right.y * (float) Mouse.getDelta().x * panSpeed * distance;
					offset.z -= right.z * (float) Mouse.getDelta().x * panSpeed * distance;

					offset.x += up.x * (float) Mouse.getDelta().y * panSpeed * distance;
					offset.y += up.y * (float) Mouse.getDelta().y * panSpeed * distance;
					offset.z += up.z * (float) Mouse.getDelta().y * panSpeed * distance;
				} else {
					mouseOffset.y += Mouse.getDelta().x;
					mouseOffset.x -= Mouse.getDelta().y;
					mouseOffset.x = Util.clamp(mouseOffset.x, -90, -10);
				}
			}

			distance += moveSpeed * Mouse.getScroll();
			distance = Util.clamp(distance, 70, 300);
		} else {
			offset.set(target.position);
		}

		if (!activated) {
			mouseOffset.y += 0.05f;
		}

		lx = Util.lerp(lx, rotOffset.x, smoothness);
		ly = Util.lerp(ly, rotOffset.y, smoothness);
		ldistance = Util.lerp(ldistance, distance, smoothness);
		lOffset = Util.lerp(lOffset, offset, smoothness);

		camera.position.x = (float) (ldistance * Math.cos(lx) * Math.sin(ly));
		camera.position.y = (float) (ldistance * Math.sin(lx));
		camera.position.z = (float) (ldistance * Math.cos(lx) * Math.cos(ly));

		camera.position.x += lOffset.x;
		camera.position.y += lOffset.y;
		camera.position.z += lOffset.z;

		camera.rotation.x = Util.lerp(camera.rotation.x, (float) -Math.toDegrees(rotOffset.x), smoothness);
		camera.rotation.y = Util.lerp(camera.rotation.y, (float) Math.toDegrees(rotOffset.y), smoothness);
	}
}