package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Skull extends Component {

    public Skull(Vector3f position, Entity parent) {
        super(parent);

        BranchGroup skullBG = Util.load3DModel("res/models/maze-stuff/skull.obj");
        Shape3D skullShape = (Shape3D) skullBG.getChild(0);
        skullShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.BLACK, 0, Game.COLOR_PALETTE));
        
        parent.entityTG.addChild(skullBG);
        parent.entityTransform.setTranslation(position);

        parent.superUpdate();
    }

    public void update() {
    }

}
