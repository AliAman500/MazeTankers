package ECS;

import java.util.LinkedList;

import org.jogamp.java3d.BranchGroup;
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
        BranchGroup bg = (BranchGroup) e.entityTG.getParent();
        bg.detach();
        sceneTG.removeChild(bg);
    }

    public Entity addEntity(Entity e) {
        entities.add(e);
        return e;
    }

    public Entity getEntity(int i) {
    	Entity e = null;
    	try {
    	e = entities.get(i);
    	} catch(Exception ex) {
    		
    	}
        return e;
    }

    public void update() {
        for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
    }
}
