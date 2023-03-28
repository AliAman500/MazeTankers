package components;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import enums.TankColor;
import tools.Util;

public class Tank extends Component {
    private TransformGroup turretTG, gunTG;
    public Transform3D bodyRotTransform, bodyPositionTransform, turretTransform, gunTransform;

    public Vector3f position;
    public String username;
    public boolean die = false;
    
    public float direction;
    public float turretDirection;
    public float gunOffset;
    public PlayerController controller;

    public Tank(String username, Vector3f position, TankColor color, Entity parent) {
        super(parent);

        this.username = username;
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

        String colorStr = ("" + color).toLowerCase();

        BranchGroup bodyBG = Util.load3DModel("res/models/" + colorStr + "-tank/body.obj");
        BranchGroup turretBG = Util.load3DModel("res/models/" + colorStr + "-tank/turret.obj");
        BranchGroup gunBG = Util.load3DModel("res/models/" + colorStr + "-tank/gun.obj");

        Appearance appearance = Util.createAppearance(Util.WHITE, Util.GREY, new Color3f(), 32, Game.COLOR_PALETTE);

        Shape3D bodyShape = (Shape3D) bodyBG.getChild(0);
        bodyShape.setAppearance(appearance);

        Shape3D turretShape = (Shape3D) turretBG.getChild(0);
        turretShape.setAppearance(appearance);

        Shape3D gunShape = (Shape3D) gunBG.getChild(0);
        gunShape.setAppearance(appearance);

        parent.entityTG.addChild(bodyBG);

        Transform3D turretOffset = new Transform3D();
        turretOffset.setTranslation(new Vector3f(0, 0.482142f, -0.356371f));
        TransformGroup turretPosTG = new TransformGroup(turretOffset);
        turretTG.addChild(turretBG);
        turretPosTG.addChild(turretTG);

        Transform3D gunOffset = new Transform3D();
        gunOffset.setTranslation(new Vector3f(0, 1.13528f, -1.0738f));
        TransformGroup gunPosTG = new TransformGroup(gunOffset);
        gunTG.addChild(gunBG);
        gunPosTG.addChild(gunTG);

        turretTG.addChild(gunPosTG);
        parent.entityTG.addChild(turretPosTG);
    }

    public Vector3f getGunWorld() {
        Transform3D t = new Transform3D();
        Vector3f p = new Vector3f();

        gunTG.getLocalToVworld(t);
        t.get(p);

        return p;
    }
    
    public float dieSpeed = 0.025f;
    private float scale = 1;
    
    public void update() {
        bodyPositionTransform.setTranslation(position);
        bodyRotTransform.rotY(Math.toRadians(direction));
        parent.entityTransform.mul(bodyPositionTransform, bodyRotTransform);

        turretTransform.rotY(Math.toRadians(turretDirection));
        gunTransform.setTranslation(new Vector3f(0, 0, gunOffset));

        turretTG.setTransform(turretTransform);
        gunTG.setTransform(gunTransform);
        
        parent.entityTransform.setScale(scale);
        if(die) {
        	controller = (PlayerController) parent.getComponent("PlayerController");
        	scale -= dieSpeed;
        	direction -= 15;
        	
        	if(scale <= 0) {
        		if(controller != null) {
        			CameraController.playerDied = true;
        		}
        		dieSpeed = 0;
        		Game.leaderBoard.addFirst(username);
        		Game.eSystem.removeEntity(Game.sceneTG, parent);
        	}
        }
        
        parent.superUpdate();
    }
}