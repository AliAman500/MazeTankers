package ECS;

import java.util.LinkedList;

public abstract class Component {
    protected Entity parent;

    public Component(Entity parent) {
        this.parent = parent;
    }
    
    public abstract void update(LinkedList<Entity> entities);
}
