package tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jdesktop.j3d.examples.sound.BackgroundSoundBehavior;
import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BackgroundSound;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.DirectionalLight;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.MediaContainer;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PointSound;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

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

	public static void enableAudio(SimpleUniverse simple_U) {
		JOALMixer mixer = null; // create a joalmixer
		Viewer viewer = simple_U.getViewer();
		viewer.getView().setBackClipDistance(20.0f);// disappear beyond 20f
		if (mixer == null && viewer.getView().getUserHeadToVworldEnable()) {
			mixer = new JOALMixer(viewer.getPhysicalEnvironment());
			if (!mixer.initialize()) {// add audio device
				System.out.println("Open AL failed to init");
				viewer.getPhysicalEnvironment().setAudioDevice(null);
			}
		}
	}

	public static void closeAudioDevice(SimpleUniverse su) {
		su.getViewer().getPhysicalEnvironment().getAudioDevice().close();
	}

	public static Node createMazeFromImage(String filepath) throws Exception {
		TransformGroup mazeTG = new TransformGroup();
		BufferedImage img = ImageIO.read(new File(filepath));
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);
				if (pixel != -1) {
					Transform3D transform = new Transform3D();
					transform.setTranslation(new Vector3f(x * 1, 0, y * 1));
					TransformGroup boxTG = new TransformGroup(transform);
					if (pixel == -16777216) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.ORANGE)));
					} else if (pixel == -65536) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.RED)));
					} else if (pixel == -16711936) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.GREEN)));
					} else if (pixel == -16776961) {
						boxTG.addChild(new Box(0.5f, 2.5f, 0.5f, Primitive.GENERATE_NORMALS, Util.solidAppearance(Util.BLUE)));
					}
					mazeTG.addChild(boxTG);
				}
			}
		}
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.5f);
		mazeTG.setTransform(scaler);
		return mazeTG;
	}
	
	public static BackgroundSound bkgdSound(String name) {
		BackgroundSound bgs = new BackgroundSound();// create a background sound
		bgs.setInitialGain(1.0f);// lower its volume
		BackgroundSoundBehavior player = // create the sound behavior
				new BackgroundSoundBehavior(bgs, locateSound(name));
				// new BackgroundSoundBehavior(bgs, "src/audio.mp3");
		player.setSchedulingBounds(lightBounds);// set scheduling bound
		return bgs;
	}

	public static URL locateSound(String fn) {
		URL url = null;
		String filename = "res/audio/" + fn + ".wav";// specify file location
		try {// locate the file
			url = new URL("file", "localhost", filename);
		} catch (Exception e) {
			System.out.println("Can't open " + filename);
		}
		return url;
	}

	public static PointSound pointSound(String name) {
		MediaContainer pointMedia = new MediaContainer(locateSound(name));
		pointMedia.setCacheEnable(true); // enable cache
		PointSound pointSound = new PointSound();// create point sound
		pointSound.setSoundData(pointMedia);// set sound file
		pointSound.setEnable(true);// enable sound
		pointSound.setInitialGain(1.0f);// set initial gain
		pointSound.setLoop(-1);// 0: once; -1: loop
		pointSound.setSchedulingBounds(lightBounds);// set schuduling
		return pointSound;
	}
	
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