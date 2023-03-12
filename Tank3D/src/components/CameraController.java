package components;

import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;
import tools.Util;

public class CameraController extends Component {
	private Camera camera;
	public Tank target;
	public Vector3f offset;
	
    public CameraController(Tank target, Entity parent) {
        super(parent);
        camera = (Camera) parent.getComponent("Camera");

        this.target = target;
    }

    public void update(ESystem eSystem) {
        camera.rotation.x = -45;
    	camera.position.x = Util.lerp(camera.position.x, target.position.x, 0.05f);
    	camera.position.z = Util.lerp(camera.position.z, target.position.z + 35, 0.05f);
    }
}
