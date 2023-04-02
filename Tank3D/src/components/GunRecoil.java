package components;

import ECS.*;

public class GunRecoil extends Component {
	public float speed = 0.42f;
	private float x = 0;
	private boolean playing = false;
	
	public boolean isPlaying() {
		return playing;
	}

	private Tank tank;
	private Audio gunShot;
	private boolean shot = false;
	
	public GunRecoil(Entity parent) {
		super(parent);
		
		gunShot = (Audio) parent.getComponent("Audio");
		tank = (Tank) parent.getComponent("Tank");
	}

	public void update() {
		if (shot) {
			gunShot.play();
			shot = false;
		}

		if(playing) {
			x += speed;
			tank.gunOffset = 0.5f * (float) Math.cos(x + Math.PI) + 0.5f;
			if(x >= 2 * Math.PI) {
				tank.gunOffset = 0;
				x = 0;
				playing = false;
				gunShot.pause();
			}
		}
	}
	
	public void playRecoil() {
		playing = true;
		shot = true;
	}
}
