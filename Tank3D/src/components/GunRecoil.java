package components;

import ECS.*;

public class GunRecoil extends Component {
	public float speed = 0.6f;
	
	private float x = 0;
	public boolean playing = false;
	
	public Tank tank;
	
	
	public GunRecoil(Entity parent) {
		super(parent);
		tank = (Tank) parent.getComponent("Tank");
	}

	public void update() {
		if(playing) {
			x += speed;
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
