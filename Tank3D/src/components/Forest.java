package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Forest extends Component {
    public Forest(Vector3f position, Entity parent) {
        super(parent);

        BranchGroup forestBG = Util.load3DModel("res/models/maze-stuff/forest.obj");
        Shape3D forestShape = (Shape3D) forestBG.getChild(0);
        forestShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.BLACK, 0, Game.COLOR_PALETTE));
        
        parent.entityTG.addChild(forestBG);
        parent.entityTransform.setTranslation(position);

        parent.superUpdate();
    }

    public void update() {
    }
}
