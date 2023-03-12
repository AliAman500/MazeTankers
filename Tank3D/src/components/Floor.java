package components;

import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;
import tools.Util;

public class Floor extends Component {
    public Floor(Entity parent) {
        super(parent);

        parent.entityTG.addChild(new Box(100, 0.2f, 100, Primitive.GENERATE_NORMALS, Util.solidAppearance(new Color3f(0.1f, 0.3f, 0.1f))));
        parent.entityTransform.setTranslation(new Vector3f(100, -3, 100));
        
        parent.superUpdate();
    }

    public void update(ESystem eSystem) {
    }
}
