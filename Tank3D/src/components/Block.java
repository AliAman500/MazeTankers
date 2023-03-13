package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Block extends Component {
    public Block(Vector3f position, Entity parent) {
        super(parent);
        
        BranchGroup blockBG = Util.load3DModel("res/models/maze-stuff/block.obj");
        Shape3D blockShape = (Shape3D) blockBG.getChild(0);
        blockShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, 0, Game.COLOR_PALETTE));

        parent.entityTG.addChild(blockBG);
        parent.entityTransform.setTranslation(position);

        parent.superUpdate();
    }

    public void update(ESystem eSystem) {
    }
}
