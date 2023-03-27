package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import tools.Util;

public class Floor extends Component {
    public Floor(Entity parent) {
        super(parent);

        BranchGroup floorBG = Util.load3DModel("res/models/maze-stuff/floor.obj");
        Shape3D floorShape = (Shape3D) floorBG.getChild(0);
        floorShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.GREY,
        0, Util.loadTexture("res/textures/floor2.png")));
        
        parent.entityTG.addChild(floorBG);
        parent.entityTransform.setTranslation(new Vector3f(78, -3, 78));
        parent.entityTransform.setScale(new Vector3d(80, 0.2f, 80));
        
        parent.superUpdate();
    }

    public void update() {
    }
}