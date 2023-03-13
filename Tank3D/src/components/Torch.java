package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Torch extends Component {
    public Torch(Vector3f position, Entity parent) {
        super(parent);
        
        BranchGroup torch = Util.load3DModel("res/models/maze-stuff/torch.obj");
        Shape3D torchShape = (Shape3D) torch.getChild(0);
        torchShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, new Color3f(0.2f, 0.2f, 0.2f), 0, Game.COLOR_PALETTE));

        BranchGroup torchFire = Util.load3DModel("res/models/maze-stuff/torch-fire.obj");
        Shape3D torchFireShape = (Shape3D) torchFire.getChild(0);
        torchFireShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.WHITE, 0, Game.COLOR_PALETTE));

        torch.addChild(torchFire);
        parent.entityTG.addChild(torch);
        parent.entityTransform.setTranslation(position);

        parent.superUpdate();
    }

    public void update() {
    }
}
