package components;

import ECS.Component;
import ECS.Entity;

public class BackgroundEntity extends Component {
    private Camera c;

    public BackgroundEntity(Entity camera, Entity parent) {
        super(parent);

        c = (Camera) camera.getComponent("Camera");
        parent.entityTransform.setTranslation(c.position);

        parent.superUpdate();
    }

    public void update() {
        parent.entityTransform.setTranslation(c.position);
        parent.superUpdate();
    }
}
