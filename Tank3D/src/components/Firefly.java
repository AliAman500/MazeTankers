package components;

import java.util.Random;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Firefly extends Component {
    private Vector3f position;
    private Material mtl;
    float elapsedTime = 0;
    float power;
    float speed;
    float radius;
    private float initialOpacity;
    private Vector3f randomAngles;
    private Vector3f randomDirs;

    public Firefly(Vector3f position, Entity parent) {
        super(parent);
        this.position = position;

        this.randomAngles = new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat());
        randomAngles.x *= new Random().nextBoolean() ? 1 : -1;
        randomAngles.y *= new Random().nextBoolean() ? 1 : -1;
        randomAngles.z *= new Random().nextBoolean() ? 1 : -1;
        
        randomDirs = new Vector3f();
        randomDirs.x = new Random().nextBoolean() ? 1 : -1;
        randomDirs.y = new Random().nextBoolean() ? 1 : -1;
        randomDirs.z = new Random().nextBoolean() ? 1 : -1;
        
        power = 6;
        speed = 0.03f * new Random().nextFloat();
        initialOpacity = new Random().nextFloat();
        radius = 0.04f;

        BranchGroup fireflyBG = Util.load3DModel("res/models/maze-stuff/firefly.obj");
		Shape3D fireflyShape = (Shape3D) fireflyBG.getChild(0);
		fireflyShape.setAppearance(
				Util.createAppearance(Util.mix(Util.BLACK, Util.WHITE, initialOpacity), Util.BLACK, Util.mix(Util.BLACK, Util.WHITE, initialOpacity), 0, Game.COLOR_PALETTE));
        mtl = fireflyShape.getAppearance().getMaterial();        

		parent.entityTG.addChild(fireflyBG);
		parent.entityTransform.setTranslation(position);
		parent.superUpdate();
    }

    public void update() {
        elapsedTime += speed;

        // firefly movement:
        float opacity = (float) Math.pow(Math.sin(elapsedTime + initialOpacity), power);
        float angle = elapsedTime;
        
        float x = position.x + radius * (float) Math.sin(randomDirs.x * angle + randomAngles.x);
        float y = position.y + radius * (float) Math.sin(randomDirs.y * angle * 2.0f + randomAngles.y);
        float z = position.z + radius * (float) Math.cos(randomDirs.z * angle + randomAngles.z);
        
        position.set(x, y, z);

        mtl.setDiffuseColor(Util.mix(Util.BLACK, Util.WHITE, opacity));
        mtl.setEmissiveColor(Util.mix(Util.BLACK, Util.WHITE, opacity));
        parent.entityTransform.setTranslation(position);
		parent.superUpdate();
    }
}
