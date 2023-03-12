package components;

import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;

public class Camera extends Component {
    private SimpleUniverse simpleUniverse;

    private Transform3D positionTransform;
    private Transform3D rotationTransform;

    public Vector3f position;
    public Vector3f rotation;
    public float FOV;

    public Camera(Vector3f position, Vector3f rotation, float FOV, SimpleUniverse simpleUniverse, Entity parent) {
        super(parent);

        this.position = position;
        this.rotation = rotation;
        this.FOV = FOV;
        this.simpleUniverse = simpleUniverse;

        positionTransform = new Transform3D();
        rotationTransform = new Transform3D();

        simpleUniverse.getViewer().getView().setFieldOfView(Math.toRadians(FOV));
        simpleUniverse.getViewer().getView().setBackClipDistance(1000);

        parent.entityTG = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
    }

    public void update(ESystem eSystem) {
        simpleUniverse.getViewer().getView().setFieldOfView(Math.toRadians(FOV));

        positionTransform.setTranslation(position);
        rotationTransform.setEuler(new Vector3d(Math.toRadians(rotation.x), Math.toRadians(rotation.y), Math.toRadians(rotation.z)));

		parent.entityTransform.mul(positionTransform, rotationTransform);
        
	    parent.superUpdate();
    }
}
