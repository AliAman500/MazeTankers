package ECS;

public abstract class Component {
    public Entity parent;

    public Component(Entity parent) {
        this.parent = parent;
    }
    
    public abstract void update();
}
