package components;

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

        parent.entityTG.addChild(new Box(100, 0.2f, 100, Primitive.GENERATE_NORMALS,
            Util.createAppearance(new Color3f(0.16f, 0.17f, 0.204f), Util.BLACK, new Color3f(0.2f, 0.2f, 0.2f), 0, null)));
        parent.entityTransform.setTranslation(new Vector3f(97, -3, 100));
        
        parent.superUpdate();
    }

    public void update() {
    }
}
