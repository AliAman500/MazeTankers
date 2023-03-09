package components;

import java.util.LinkedList;

import org.jogamp.vecmath.Vector3f;

import ECS.Component;
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
        this.offset = new Vector3f(0, 0, 20);
    }

    public void update(LinkedList<Entity> entities) {
        camera.rotation.x = -45;
    	camera.position.x = Util.lerp(camera.position.x, target.position.x, 0.05f);
    	camera.position.z = Util.lerp(camera.position.z, target.position.z + 35, 0.05f);
    }
}
