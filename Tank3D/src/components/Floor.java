package components;

import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import tools.Util;

public class Floor extends Component {
    public Floor(Entity parent) {
        super(parent);
        Box box = new Box(80, 0.2f, 80, Primitive.GENERATE_NORMALS,
            Util.createAppearance(new Color3f(0.16f, 0.17f, 0.204f), Util.BLACK, new Color3f(0, 0, 0), 0, null));
        parent.entityTG.addChild(box);
        parent.entityTransform.setTranslation(new Vector3f(78, -3, 78));
        
        parent.superUpdate();
    }

    public void update() {
    }
}
