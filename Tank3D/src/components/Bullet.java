package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Bullet extends Component {

	public Vector3f target;
	public Vector3f position;
	public Vector3f startPosition;
	private float distance = 0;
	private float speed = 0.3f;
	public float life = 100;
	public String username;
	
	public Bullet(Entity parent, Vector3f position, Vector3f target) {
		super(parent);
		this.target = target;
		this.position = position;
		startPosition = new Vector3f(position);
		BranchGroup torch = Util.load3DModel("res/models/red-tank/body.obj");
		Shape3D torchShape = (Shape3D) torch.getChild(0);
		torchShape.setAppearance(
				Util.createAppearance(Util.WHITE, Util.BLACK, new Color3f(0.2f, 0.2f, 0.2f), 0, Game.COLOR_PALETTE));

		BranchGroup torchFire = Util.load3DModel("res/models/red-tank/turret.obj");
		Shape3D torchFireShape = (Shape3D) torchFire.getChild(0);
		torchFireShape.setAppearance(Util.createAppearance(Util.WHITE, Util.BLACK, Util.WHITE, 0, Game.COLOR_PALETTE));

		torch.addChild(torchFire);
		parent.entityTG.addChild(torch);
		parent.entityTransform.setTranslation(position);
		parent.entityTransform.setScale(0.2f);
		parent.superUpdate();
		distance = (float) Math.sqrt(
				(target.x - position.x) * (target.x - position.x) + (target.z - position.z) * (target.z - position.z));
	}

	public void update() {
		double dx = (target.x - startPosition.x) / distance;
		double dz = (target.z - startPosition.z) / distance;
		double vx = dx * speed;
		double vz = dz * speed;
		position.x += vx;
		position.z += vz;

		parent.entityTransform.setTranslation(position);
		parent.superUpdate();
		life--;
		if(life <= 0) {
			Game.eSystem.removeEntity(Game.sceneTG, parent);
		}
	}

}
