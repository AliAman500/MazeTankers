package components;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;

public class GunRecoil extends Component {

	public float speed = 1f;
	
	private float x = 0;
	private boolean playing = false;
	
	public Tank tank;
	
	public GunRecoil(Entity parent) {
		super(parent);
		
		tank = (Tank) parent.getComponent("Tank");
	}

	public void update(ESystem eSystem) {
		if(playing) {
			x += 0.45f;
			tank.gunOffset = 0.5f * (float) Math.cos(x + Math.PI) + 0.5f;
			if(x >= 2 * Math.PI) {
				tank.gunOffset = 0;
				x = 0;
				playing = false;
			}
		}
	}
	
	public void playRecoil() {
		playing = true;
	}
}
