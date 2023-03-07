package tools;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.universe.*;

public class Util {
	public final static Color3f RED = new Color3f(1.0f, 0.0f, 0.0f);
	public final static Color3f GREEN = new Color3f(0.0f, 1.0f, 0.0f);
	public final static Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);
	public final static Color3f YELLOW = new Color3f(1.0f, 1.0f, 0.0f);
	public final static Color3f CYAN = new Color3f(0.0f, 1.0f, 1.0f);
	public final static Color3f ORANGE = new Color3f(1.0f, 0.5f, 0.0f);
	public final static Color3f MAGENTA = new Color3f(1.0f, 0.0f, 1.0f);
	public final static Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);
	public final static Color3f GREY = new Color3f(0.35f, 0.35f, 0.35f);
	public final static Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);
	
    private static Color3f[] mtlColors = { WHITE, GREY, BLACK };

	public final static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
	public final static BoundingSphere twentyBS = new BoundingSphere(new Point3d(), 20.0);

	public static Appearance solidAppearance(Color3f color) {		
		Material mtl = new Material();
		mtl.setShininess(32);
		mtl.setAmbientColor(mtlColors[0]);
		mtl.setDiffuseColor(color);
		mtl.setSpecularColor(mtlColors[1]);
		mtl.setEmissiveColor(mtlColors[2]);
		mtl.setLightingEnable(true);

		Appearance app = new Appearance();
		app.setMaterial(mtl);
		return app;
	}	
	
	public static void defineViewer(SimpleUniverse simpleUniverse, Point3d eye) {
	    TransformGroup viewTransform = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
		Point3d center = new Point3d(0, 0, 0);
		Vector3d up = new Vector3d(0, 1, 0);
		Transform3D viewTM = new Transform3D();
		viewTM.lookAt(eye, center, up);
		viewTM.invert();
	    viewTransform.setTransform(viewTM); 
	}
}