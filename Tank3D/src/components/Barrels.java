package components;

import java.util.Random;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Barrels extends Component {

    public Barrels(Vector3f position, Entity parent) {
        super(parent);

        BranchGroup barrelsBG = Util.load3DModel("res/models/maze-stuff/barrels.obj");
        Shape3D barrelsShape = (Shape3D) barrelsBG.getChild(0);
        barrelsShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.BLACK, 0, Game.COLOR_PALETTE));

        Transform3D posT = new Transform3D();
        posT.setTranslation(position);

        Transform3D rotT = new Transform3D();
        rotT.setEuler(new Vector3d(0, Math.toRadians(new Random().nextInt(360)), 0));

        parent.entityTG.addChild(barrelsBG);
        parent.entityTransform.mul(posT, rotT);

        parent.superUpdate();
    }

    public void update() {
    }
    
}
