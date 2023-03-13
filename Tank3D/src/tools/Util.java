package tools;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;

import org.jdesktop.j3d.examples.sound.BackgroundSoundBehavior;
import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;

import ECS.ESystem;
import ECS.Entity;
import entities.Entities;

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

	public final static BoundingSphere LIGHT_BOUNDS = new BoundingSphere(new Point3d(), 1000.0);

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

	public static Entity setupMaze(String filepath, TransformGroup sceneTG, ESystem eSystem) throws Exception {
		BufferedImage img = ImageIO.read(new File(filepath));
		Entity userTank = null;

		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);

				int red = (pixel >> 16) & 255;
                int green = (pixel >> 8) & 255;
                int blue = pixel & 255;

				if (red == 255 && green == 0 && blue == 0)
					eSystem.addEntity(userTank = Entities.createUserTank(sceneTG, new Vector3f(x * 4, 0, y * 4)));

				if (red == 0 && green == 0 && blue == 0)
					eSystem.addEntity(Entities.createBlock(sceneTG, new Vector3f(x * 4, 3, y * 4)));

				if (red == 0 && green == 0 && blue == 255) {
					eSystem.addEntity(Entities.createNetworkTank(sceneTG, new Vector3f(x * 4, 0, y * 4)));
				}
			}
		}

		return userTank;
	}
	
	public static BackgroundSound createBackgroundSound(String filepath) {
		BackgroundSound bgs = new BackgroundSound();
		bgs.setContinuousEnable(true);
		bgs.setInitialGain(0.15f);
		bgs.setLoop(Sound.INFINITE_LOOPS);
		BackgroundSoundBehavior player = new BackgroundSoundBehavior(bgs, locateSound(filepath));
		bgs.setBounds(LIGHT_BOUNDS);
		player.setBounds(LIGHT_BOUNDS);
		return bgs;
	}

	public static URL locateSound(String filepath) {
		URL url = null;
		try {// locate the file
			url = new URL("file", "localhost", filepath);
		} catch (Exception e) {
			System.out.println("Can't open " + filepath);
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
		pointSound.setSchedulingBounds(LIGHT_BOUNDS);// set schuduling
		return pointSound;
	}

	public static Appearance createAppearance(Color3f diffuse, Color3f specular, float shininess, TextureData texData) {
		Material mtl = new Material();

		mtl.setShininess(shininess);
		mtl.setAmbientColor(WHITE);
		mtl.setDiffuseColor(diffuse);
		mtl.setSpecularColor(specular);
		mtl.setEmissiveColor(new Color3f(0.2f, 0.2f, 0.2f));
		mtl.setLightingEnable(true);

		Appearance appearance = new Appearance();
		appearance.setMaterial(mtl);

		if (texData != null) {
			appearance.setTexture(texData.texture);
			appearance.setTextureAttributes(texData.texAttr);
		}

		return appearance;
	}

	public static void addDirectionalLight(BranchGroup lightGroup, Vector3f direction, Color3f color) {
		DirectionalLight directionalLight = new DirectionalLight(color, direction);
		directionalLight.setInfluencingBounds(LIGHT_BOUNDS);
		lightGroup.addChild(directionalLight);
	}

	public static TextureData loadTexture(String filepath) {
		TextureData data = new TextureData();
		
		TextureLoader loader = new TextureLoader(filepath, null);
		data.texture = (Texture2D) loader.getTexture();
		data.texAttr = new TextureAttributes();

        data.texAttr.setTextureMode(TextureAttributes.MODULATE);

		return data;
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