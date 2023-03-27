package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector2f;
import org.jogamp.vecmath.Vector3f;

import ECS.Component;
import ECS.Entity;
import entry.Game;
import tools.Util;

public class Bullet extends Component {

	public float direction;
	public Vector3f position;
	public Vector3f startPosition;
	private float speed = 0.3f;
	public float life = 780;
	public String username;
	private float spawnDistance = 1;
	private float velX = 0, velZ = 0;
	private BoxCollider boxCollider;
	
	private boolean collided = false;
	private boolean collided2 = false;
	private boolean collided3 = false;
	private boolean collided4 = false;

	public boolean active = false;
	private BoxCollider shooter;
	private Audio wallHit;
	
	public Bullet(Entity parent, Vector3f position, BoxCollider shooter, float direction) {
		super(parent);
		this.direction = direction;
		this.shooter = shooter;
		position.x -= spawnDistance * Math.sin(Math.toRadians(direction));
		position.z -= spawnDistance * Math.cos(Math.toRadians(direction));
		this.position = new Vector3f(position.x, position.y, position.z);
		startPosition = new Vector3f(position);
		boxCollider = (BoxCollider) parent.getComponent("BoxCollider");
		wallHit = (Audio) parent.getComponent("Audio");
		BranchGroup bulletBG = Util.load3DModel("res/models/bullet.obj");
		Shape3D bulletShape = (Shape3D) bulletBG.getChild(0);
		bulletShape.setAppearance(
				Util.createAppearance(Util.WHITE, Util.BLACK, new Color3f(0.5f, 0.5f, 0.5f), 0, Game.COLOR_PALETTE));

		parent.entityTG.addChild(bulletBG);
		parent.entityTransform.setTranslation(position);
		parent.entityTransform.setScale(0.35f);
		parent.superUpdate();
		velX = (float) (speed * Math.cos(Math.toRadians(direction + 90)));
		velZ = -(float) (speed * Math.sin(Math.toRadians(direction + 90)));
	}

	public void update() {
		parent.entityTransform.setTranslation(position);
		parent.superUpdate();
		life--;
		if (life <= 0) {
			Game.eSystem.removeEntity(Game.sceneTG, parent);
		}

		for (int i = 0; i < Game.eSystem.numEntities(); i++) {
			Entity e = Game.eSystem.getEntity(i);
			Block b = (Block) e.getComponent("Block");

			if (b != null) {
				BoxCollider bc = (BoxCollider) e.getComponent("BoxCollider");

				collided = BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsRight(b.position));
				collided2 = BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsLeft(b.position));
				collided3 = BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsTop(b.position));
				collided4 = BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsBottom(b.position));
			}
		}
		Tank shooterTank = (Tank) shooter.parent.getComponent("Tank");
		if(!BoxCollider.collision(shooter.getBounds(shooterTank.position), boxCollider.getBounds(position)) && !active) {
			active = true;
		}
		
		for (int i = 0; i < Game.eSystem.numEntities(); i++) {
			Entity e = Game.eSystem.getEntity(i);
			Block b = (Block) e.getComponent("Block");
			BoxCollider bc = (BoxCollider) e.getComponent("BoxCollider");

			if (b != null) {

				if (BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsRight(b.position)) && !collided) {
					collided = true;
					position.x = (b.position.x + 2) + 0.7f;
					velX = -velX;
					wallHit.play();
				}

				else if (BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsLeft(b.position)) && !collided2) {
					collided2 = true;
					position.x = (b.position.x - 2) - 0.7f;
					velX = -velX;
					wallHit.play();
				}

				else if (BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsTop(b.position)) && !collided3) {
					collided3 = true;
					position.z = (b.position.z - 2) - 0.7f;
					velZ = -velZ;
					wallHit.play();
				}

				else if (BoxCollider.collision(0.35f, new Vector2f(position.x, position.z), bc.getBoundsBottom(b.position)) && !collided4) {
					collided4 = true;
					position.z = (b.position.z + 2) + 0.7f;
					velZ = -velZ;
					wallHit.play();
				}
			}
			
			Tank tank = (Tank) e.getComponent("Tank");
			
			if(tank != null) {
				if(BoxCollider.collision(bc.getBounds(tank.position), boxCollider.getBounds(position)) && active) {
					Game.eSystem.removeEntity(Game.sceneTG, parent);
					tank.die = true;
				}
			}
		}

		position.x += velX;
		position.z += velZ;
	}

}