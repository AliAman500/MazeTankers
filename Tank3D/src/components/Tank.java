package components;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;
import entry.Game;
import enums.TankColor;
import tools.Util;

public class Tank extends Component {
    private TransformGroup turretTG, gunTG;
    public Transform3D bodyRotTransform, bodyPositionTransform, turretTransform, gunTransform;

    public Vector3f position;

    public float direction;
    public float turretDirection;
    public float gunOffset;

    public Tank(Vector3f position, TankColor color, Entity parent) {
        super(parent);
        
        this.position = position;
        this.direction = 0;
        this.turretDirection = 0;
        this.gunOffset = 0;

        turretTransform = new Transform3D();
        gunTransform = new Transform3D();
        bodyRotTransform = new Transform3D();
        bodyPositionTransform = new Transform3D();

        turretTG = new TransformGroup();
        gunTG = new TransformGroup();

        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        gunTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        String colorStr = color == TankColor.RED ? "red" : "blue";

        BranchGroup bodyBG = Util.load3DModel("res/models/" + colorStr + "-tank/body.obj");
        BranchGroup turretBG = Util.load3DModel("res/models/" + colorStr + "-tank/turret.obj");
        BranchGroup gunBG = Util.load3DModel("res/models/" + colorStr + "-tank/gun.obj");

        Shape3D bodyShape = (Shape3D) bodyBG.getChild(0);
        bodyShape.setAppearance(Game.COLOR_PALETTE);

        Shape3D turretShape = (Shape3D) turretBG.getChild(0);
        turretShape.setAppearance(Game.COLOR_PALETTE);

        Shape3D gunShape = (Shape3D) gunBG.getChild(0);
        gunShape.setAppearance(Game.COLOR_PALETTE);

        parent.entityTG.addChild(bodyBG);

        Transform3D turretOffset = new Transform3D();
        turretOffset.setTranslation(new Vector3f(-0.27f, 0.482142f, -0.356371f));
        TransformGroup turretPosTG = new TransformGroup(turretOffset);
        turretTG.addChild(turretBG);
        turretPosTG.addChild(turretTG);

        Transform3D gunOffset = new Transform3D();
        gunOffset.setTranslation(new Vector3f(0.028283f, 1.13528f, -1.0738f));
        TransformGroup gunPosTG = new TransformGroup(gunOffset);
        gunTG.addChild(gunBG);
        gunPosTG.addChild(gunTG);

        turretTG.addChild(gunPosTG);
        parent.entityTG.addChild(turretPosTG);
    }

    public void update(ESystem eSystem) {
        bodyPositionTransform.setTranslation(position);
        bodyRotTransform.rotY(Math.toRadians(direction));
        parent.entityTransform.mul(bodyPositionTransform, bodyRotTransform);

        turretTransform.rotY(Math.toRadians(turretDirection));
        gunTransform.setTranslation(new Vector3f(0, 0, gunOffset));

        parent.superUpdate();
        turretTG.setTransform(turretTransform);
        gunTG.setTransform(gunTransform);
    }
}