package tools;

import java.io.*;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.image.TextureLoader;

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

	public final static BoundingSphere lightBounds = new BoundingSphere(new Point3d(), 1000.0);

	public static Appearance solidAppearance(Color3f color) {		
		Material mtl = new Material();
		mtl.setShininess(32);
		mtl.setAmbientColor(WHITE);
		mtl.setSpecularColor(GREY);
		mtl.setEmissiveColor(BLACK);
		mtl.setDiffuseColor(color);
		mtl.setLightingEnable(true);

		Appearance app = new Appearance();
		app.setMaterial(mtl);
		return app;
	}	

	public static void addDirectionalLight(BranchGroup lightGroup, Vector3f direction, Color3f color) {
		DirectionalLight directionalLight = new DirectionalLight(color, direction);
		directionalLight.setInfluencingBounds(lightBounds);
		lightGroup.addChild(directionalLight);
	}

	public static Appearance texturedAppearance(String filepath) {
		TextureLoader loader = new TextureLoader(filepath, null);
		Texture2D texture = (Texture2D) loader.getTexture();
		Appearance appearance = solidAppearance(WHITE);

		TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
		
		appearance.setTexture(texture);
		appearance.setTextureAttributes(texAttr);

		return appearance;
	}

	public static float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }

	public static Vector3f lerp(Vector3f start, Vector3f end, float t) {
	    float x = lerp(start.x, end.x, t);
	    float y = lerp(start.y, end.y, t);
	    float z = lerp(start.z, end.z, t);
	    return new Vector3f(x, y, z);
	}
	
	public static BranchGroup load3DModel(String filepath) {
		ObjectFile f = new ObjectFile();
		Scene s = null;
        try {
            s = f.load(filepath);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }

        return s.getSceneGroup();
	}
}