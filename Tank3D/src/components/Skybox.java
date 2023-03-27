package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import tools.Util;

public class Skybox extends Component {
    private Camera camera;

    public Skybox(Entity camera, Entity parent) {
        super(parent);
        this.camera = (Camera) camera.getComponent("Camera");

        BranchGroup skyboxBG = Util.load3DModel("res/models/background/skybox.obj");
        Shape3D skyboxShape = (Shape3D) skyboxBG.getChild(0);
        skyboxShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.WHITE, 0, Util.loadTexture("res/textures/starry-night.png")));

        // allows us to see the inside of the skybox, which is the whole point of it.
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);

        skyboxShape.getAppearance().setPolygonAttributes(pa);

        parent.entityTG.addChild(skyboxBG);
        parent.entityTransform.setScale(1000);
        parent.entityTransform.setTranslation(new Vector3f());

        parent.superUpdate();
    }

    public void update() {
        parent.entityTransform.setTranslation(camera.position);
        parent.superUpdate();
    }
}
