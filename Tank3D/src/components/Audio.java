package components;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.PointSound;
import org.jogamp.vecmath.Point3f;

import ECS.Component;
import ECS.Entity;
import tools.Util;

public class Audio extends Component {
    private BranchGroup soundBG;
    private PointSound ps;

    private float elapsedTime = 0;
    private float duration;

    private boolean counting = false;

    public Audio(String audioFile, float volume, int loop, Point3f offset, float duration, Entity parent) {
        super(parent);

        this.duration = duration;

        soundBG = new BranchGroup();
        soundBG.addChild(ps = Util.createPointSound(audioFile, volume, loop, offset));
        ps.setEnable(false);

        parent.entityTG.addChild(soundBG);
    }

    public void play() {
        counting = true;
        ps.setEnable(true);
    }

    public void pause() {
        ps.setEnable(false);
    }

    public void update() {
        if (duration != 0) {
            if (counting)
                elapsedTime += 1 / 60f;

            if (elapsedTime >= duration) {
                ps.setEnable(false);
                elapsedTime = 0;
                counting = false;
            }
        }
    }

}
