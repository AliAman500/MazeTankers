package components;

import ECS.Component;
import ECS.Entity;
import tools.Util;

public class CameraController extends Component {
    
	private Camera camera;
    public Tank target;
    public float smoothness = 0.05f;
    
    public CameraController(Tank target, Entity parent) {
        super(parent);
        camera = (Camera) parent.getComponent("Camera");

        this.target = target;
        
        camera.rotation.x = -60;
        camera.position.x = target.position.x;
        camera.position.z = target.position.z + 35;
    }

    public void update() {
        camera.position.x = Util.lerp(camera.position.x, target.position.x, smoothness);
        camera.position.z = Util.lerp(camera.position.z, target.position.z + 45, smoothness);
    }
}