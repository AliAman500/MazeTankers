package ECS;

import java.util.LinkedList;

import org.jogamp.java3d.TransformGroup;

public class ESystem {
    private LinkedList<Entity> entities;

    public ESystem() {
        entities = new LinkedList<Entity>();
    }

    public int numEntities() {
        return entities.size();
    }

    public void removeEntity(TransformGroup sceneTG, Entity e) {
        entities.remove(e);
        sceneTG.removeChild(e.entityTG);
    }

    public Entity addEntity(Entity e) {
        entities.add(e);
        return e;
    }

    public Entity getEntity(int i) {
        return entities.get(i);
    }

    public void update() {
        for (int i = 0; i < entities.size(); i++)
			entities.get(i).update(this);
    }
}
