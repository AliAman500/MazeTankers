package components;

import ECS.Component;
import ECS.Entity;
import tools.Util;

public class CameraController extends Component {
	private Camera camera;
	public Tank target;
	
    public CameraController(Tank target, Entity parent) {
        super(parent);
        camera = (Camera) parent.getComponent("Camera");

        this.target = target;
        
        camera.rotation.x = -45;
        camera.position.x = Util.lerp(camera.position.x, target.position.x, 1);
    	camera.position.z = Util.lerp(camera.position.z, target.position.z + 35, 1);
    }

    public void update() {
    	camera.position.x = Util.lerp(camera.position.x, target.position.x, 0.05f);
    	camera.position.z = Util.lerp(camera.position.z, target.position.z + 35, 0.05f);
    }
}