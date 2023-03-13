package components;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.*;
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

        String colorStr = null;
        switch (color) {
            case RED:
                colorStr = "red";
                break;
        
            case BLUE:
                colorStr = "blue";
                break;

            case CYAN:
                colorStr = "cyan";
                break;
            
            case WHITE:
                colorStr = "white";
                break;
            
            case PURPLE:
                colorStr = "pruple";
                break;
            
            case MAGENTA:
                colorStr = "magenta";
                break;
            
            case ORANGE:
                colorStr = "orange";
                break;
            
            case YELLOW:
                colorStr = "yellow";
                break;
            
            case GREEN:
                colorStr = "green";
                break;
            default:
                break;
        }

        BranchGroup bodyBG = Util.load3DModel("res/models/" + colorStr + "-tank/body.obj");
        BranchGroup turretBG = Util.load3DModel("res/models/" + colorStr + "-tank/turret.obj");
        BranchGroup gunBG = Util.load3DModel("res/models/" + colorStr + "-tank/gun.obj");

        Appearance appearance = Util.createAppearance(Util.WHITE, Util.GREY, new Color3f(0.2f, 0.2f, 0.2f), 32, Game.COLOR_PALETTE);

        Shape3D bodyShape = (Shape3D) bodyBG.getChild(0);
        bodyShape.setAppearance(appearance);

        Shape3D turretShape = (Shape3D) turretBG.getChild(0);
        turretShape.setAppearance(appearance);

        Shape3D gunShape = (Shape3D) gunBG.getChild(0);
        gunShape.setAppearance(appearance);

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

        // add headlight here:
        // direction of light will always be forward (0, 0, -1)
        // SpotLight headlight = new SpotLight(Util.WHITE, new Point3f(0, 0, -2), new Point3f(1, 0.1f, 0), new Vector3f(0, 0, -1), 45, 1);
        // headlight.setInfluencingBounds(Util.LIGHT_BOUNDS);
        // parent.entityTG.addChild(headlight);
    }

    public void update() {
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