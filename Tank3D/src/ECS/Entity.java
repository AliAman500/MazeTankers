package ECS;

import java.util.*;
import org.jogamp.java3d.*;

public class Entity {
    public Transform3D entityTransform;
    public TransformGroup entityTG;

    private Map<String, Component> components;

    public Entity(TransformGroup scene) {
        components = new HashMap<String, Component>();

        entityTransform = new Transform3D();
        entityTG = new TransformGroup(entityTransform);
        entityTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        scene.addChild(entityTG);
    }

    public Entity() {
        components = new HashMap<String, Component>();

        entityTransform = new Transform3D();
        entityTG = new TransformGroup(entityTransform);
        entityTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }

    public Component addComponent(Component c) {
        components.put(c.getClass().getSimpleName(), c);
        return c;
    }

    public Component getComponent(String className) {
        return components.get(className);
    }

    public void superUpdate() {
        entityTG.setTransform(entityTransform);
    }

    public void update() {
        for (Map.Entry<String, Component> entry : components.entrySet())
            entry.getValue().update();
    }
}
