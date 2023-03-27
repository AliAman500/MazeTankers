package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Block extends Component {
    public Vector3f position;

    public Block(Vector3f position, Entity parent) {
        super(parent);
        this.position = position;
        
        BranchGroup blockBG = Util.load3DModel("res/models/maze-stuff/block.obj");
        Shape3D blockShape = (Shape3D) blockBG.getChild(0);
        blockShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, new Color3f(0.2f, 0.2f, 0.2f), 0, Game.COLOR_PALETTE));

        parent.entityTG.addChild(blockBG);
        parent.entityTransform.setTranslation(position);

        parent.superUpdate();
    }

    public void update() {
    }
}
