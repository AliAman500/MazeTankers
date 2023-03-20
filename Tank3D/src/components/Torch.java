package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import enums.TorchDirection;
import tools.Util;

public class Torch extends Component {
    public Torch(Vector3f position, TorchDirection direction, Entity parent) {
        super(parent);
        
        BranchGroup torch = Util.load3DModel("res/models/maze-stuff/torch.obj");
        Transform3D torchDirTransform = new Transform3D();
        TransformGroup torchDirTG = new TransformGroup();
        
        Shape3D torchShape = (Shape3D) torch.getChild(0);
        torchShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, new Color3f(0.2f, 0.2f, 0.2f), 0, Game.COLOR_PALETTE));

        torchDirTG.addChild(torch);

        BranchGroup torchFire = Util.load3DModel("res/models/maze-stuff/torch-fire.obj");
        Shape3D torchFireShape = (Shape3D) torchFire.getChild(0);
        torchFireShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.WHITE, 0, Game.COLOR_PALETTE));

        Point3f att = new Point3f(1, 0.09f, 0.032f);
        PointLight light = new PointLight(Util.ORANGE, new Point3f(), att);
        Point3f offset = new Point3f(0, 3.7964f, -3.7964f);

        switch (direction) {
            case FORWARD:
                light.setPosition(offset);
                break;
        
            case BACK:
                torchDirTransform.rotY(Math.toRadians(180));
                light.setPosition(offset);
                break;
            
            case RIGHT:
                torchDirTransform.rotY(Math.toRadians(-90));
                light.setPosition(offset);
                break;
            
            case LEFT:
                torchDirTransform.rotY(Math.toRadians(90));
                light.setPosition(offset);
                break;
            default:
                break;
        }

        torchDirTG.setTransform(torchDirTransform);

        light.setInfluencingBounds(Util.LIGHT_BOUNDS);
        torchFire.addChild(light);

        torchDirTG.addChild(torchFire);
        parent.entityTG.addChild(torchDirTG);
        parent.entityTransform.setTranslation(position);

        parent.superUpdate();
    }

    public void update() {
    }
}
