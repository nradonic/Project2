package physics;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import mygame.BallState;

/**
 *
 * @author normenhansen
 */
public class PhysicsTestHelper {

    private ArrayList<BallState> runRecords = new ArrayList<BallState>();
    public static  ArrayList<Geometry> balls = new ArrayList<Geometry>();
    /**
     * creates a simple physics test world with a floor, an obstacle and some
     * test boxes
     *
     * @param rootNode
     * @param assetManager
     * @param space
     */
    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));

        Box floorBox = new Box(140, 0.25f, 140);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -5, 0);
//        Plane plane = new Plane();
//        plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
        floorGeometry.addControl(new RigidBodyControl(0));
        floorGeometry.getControl(RigidBodyControl.class).setRestitution(1);
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        //movable boxes
        for (int i = 0; i < 12; i++) {
            Box box = new Box(0.25f, 0.25f, 0.25f);
            Geometry boxGeometry = new Geometry("Box", box);
            boxGeometry.setMaterial(material);
            boxGeometry.setLocalTranslation(i, 5, -3);
            //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
            boxGeometry.addControl(new RigidBodyControl(2));
            rootNode.attachChild(boxGeometry);
            space.add(boxGeometry);
        }

        //immovable sphere with mesh collision shape
        Sphere sphere = new Sphere(8, 8, 1);
        Geometry sphereGeometry = new Geometry("Sphere", sphere);
        sphereGeometry.setMaterial(material);
        sphereGeometry.setLocalTranslation(4, -4, 2);
        sphereGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(sphere), 0));
        rootNode.attachChild(sphereGeometry);
        space.add(sphereGeometry);

    }

    public static void createPhysicsTestWorldSoccer(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));

        Box floorBox = new Box(20, 0.25f, 20);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);
//        Plane plane = new Plane();
//        plane.setOriginNormal(new Vector3f(0, 0.25f, 0), Vector3f.UNIT_Y);
//        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
        floorGeometry.addControl(new RigidBodyControl(0));
        floorGeometry.getControl(RigidBodyControl.class).setRestitution(1);
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        // lots of movable spheres NR
        for (int i = 0; i < 3; i++) {
            Sphere sphere = new Sphere(16, 16, randomSphere(3f));
            Geometry ballGeometry = new Geometry("Soccer ball", sphere);
            Material mt2 = material.clone();
            mt2.setColor("Color", splashSomeColorOnMyBrush());
            ballGeometry.setMaterial(mt2);
            ballGeometry.setLocalTranslation(scaledCosineToInt(i, 8), 10 + i, scaledSineToInt(i, 8));
            //RigidBodyControl automatically uses Sphere collision shapes when attached to single geometry with sphere mesh
            ballGeometry.addControl(new RigidBodyControl(4f)); // arbitrary mass of 4 units
            ballGeometry.getControl(RigidBodyControl.class).setRestitution(1); // coef 1= perfect bounce
            ballGeometry.getControl(RigidBodyControl.class).setLinearVelocity(new Vector3f(10*i, 0, 0));
            rootNode.attachChild(ballGeometry);
            space.add(ballGeometry);
            balls.add(ballGeometry);
        }
        // NR new boxes
//        for (int i = 1; i <= 5; i++) {
//            Vector3f location = new Vector3f(-15+5*i, (float)i/3, (float)i*i-18);
//            Vector3f size = new Vector3f((float)i/3, (float)i/2, (float)i/4);
//            Material mt2 = material.clone();
//            mt2.setColor("Color", splashSomeColorOnMyBrush());
//            Geometry box = handMeABox(location, size, 0, mt2);
//            rootNode.attachChild(box);
//            space.add(box);
//        }
//        {
//            //immovable Box with mesh collision shape
//            Box box = new Box(1, 1, 1);
//            Geometry boxGeometry = new Geometry("Box", box);
//            boxGeometry.setMaterial(material);
//            boxGeometry.setLocalTranslation(4, 1, 2);
//            boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
//            rootNode.attachChild(boxGeometry);
//            space.add(boxGeometry);
//        }
//        {
//            //immovable Box with mesh collision shape
//            Box box = new Box(1, 1, 1);
//            Geometry boxGeometry = new Geometry("Box", box);
//            boxGeometry.setMaterial(material);
//            boxGeometry.setLocalTranslation(4, 3, 4);
//            boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
//            rootNode.attachChild(boxGeometry);
//            space.add(boxGeometry);
//        }
    }

    /**
     * creates boxes with mass and interaction
     *
     * @param location
     * @author Nick Radonic
     */
    private static Geometry handMeABox(Vector3f sizeV3, Vector3f location, float mass, Material material) {
        Box box = new Box(sizeV3, location.x, location.y, location.z);
        Geometry boxGeometry = new Geometry("Box", box);
        Material mt2 = material.clone();
        mt2.setColor("Color", splashSomeColorOnMyBrush());
        boxGeometry.setMaterial(mt2);
        boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), mass));
        boxGeometry.getControl(RigidBodyControl.class).setRestitution(1);

        return boxGeometry;
    }

    /**
     * returns random RGBA color
     *
     * @return ColorRGBA
     */
    private static ColorRGBA splashSomeColorOnMyBrush() {
        float r = ((Double) Math.random()).floatValue();
        float g = ((Double) Math.random()).floatValue();
        float b = ((Double) Math.random()).floatValue();
        float a = 1.0f;
        ColorRGBA color = new ColorRGBA(r, g, b, a);
        return color;
    }

    /**
     * local integer to cosine to integer conversion
     *
     * @param countValue
     * @return integer projection
     * @author Nick Radonic
     */
    private static int scaledCosineToInt(int i, int scale) {
        return ((Double) (scale * Math.cos(i))).intValue();

    }

    /**
     * local integer to cosine to integer conversion
     *
     * @param countValue
     * @return integer projection
     * @author Nick Radonic
     */
    private static int scaledSineToInt(int i, int scale) {
        return ((Double) (scale * Math.sin(i))).intValue();

    }

    /**
     * Random sphere size
     *
     * @param size maximum sphere diameter
     * @return int random sphere size
     * @author Nick Radonic
     */
    private static float randomSphere(float size) {
        return ((Double) (Math.random() * size +3)).floatValue();
    }

    /**
     * creates a fixed wall NR
     *
     * @param assetManager
     * @return
     * @author Nick Radonic
     */
    private static void createFixedBoundaryWall(Node rootNode, AssetManager assetManager,
            PhysicsSpace space, Vector3f wallSize, Vector3f wallLocation) {
        //immovable Box with mesh collision shape
        Box box = new Box(wallSize.x, wallSize.y, wallSize.z);
        Geometry boxGeometry = new Geometry("Box", box);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        material.setColor("Color", splashSomeColorOnMyBrush());
        material.getAdditionalRenderState().setWireframe(true);

        boxGeometry.setMaterial(material);
        boxGeometry.setLocalTranslation(wallLocation.x, wallLocation.y, wallLocation.z);
        boxGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));
        boxGeometry.getControl(RigidBodyControl.class).setRestitution(1); // perfect bounce
        
        rootNode.attachChild(boxGeometry);
        space.add(boxGeometry);
    }

    /**
     * creates a box geometry with a RigidBodyControl
     *
     * @param assetManager
     * @return
     */
    public static Geometry createPhysicsTestBox(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("200px_Brick_wall_old.jpg"));
        Box box = new Box(0.25f, 0.25f, 0.25f);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);
        //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
        boxGeometry.addControl(new RigidBodyControl(2));
        return boxGeometry;
    }

    /**
     * creates a sphere geometry with a RigidBodyControl
     *
     * @param assetManager
     * @return
     */
    public static Geometry createPhysicsTestSphere(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Sphere sphere = new Sphere(8, 8, 0.25f);
        Geometry boxGeometry = new Geometry("Sphere", sphere);
        boxGeometry.setMaterial(material);
        //RigidBodyControl automatically uses sphere collision shapes when attached to single geometry with sphere mesh
        boxGeometry.addControl(new RigidBodyControl(2));
        return boxGeometry;
    }

    /**
     * creates an empty node with a RigidBodyControl
     *
     * @param manager
     * @param shape
     * @param mass
     * @return
     */
    public static Node createPhysicsTestNode(AssetManager manager, CollisionShape shape, float mass) {
        Node node = new Node("PhysicsNode");
        RigidBodyControl control = new RigidBodyControl(shape, mass);
        node.addControl(control);
        return node;
    }

    /**
     * creates the necessary inputlistener and action to shoot balls from teh
     * camera
     *
     * @param app
     * @param rootNode
     * @param space
     */
    public static void createBallShooter(final Application app, final Node rootNode, final PhysicsSpace space) {
        ActionListener actionListener = new ActionListener() {
            public void onAction(String name, boolean keyPressed, float tpf) {
                Sphere bullet = new Sphere(22, 22, 0.4f, true, false);
                bullet.setTextureMode(TextureMode.Projected);
                Material mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
                key2.setGenerateMips(true);
                Texture tex2 = app.getAssetManager().loadTexture(key2);
                mat2.setTexture("ColorMap", tex2);
                mat2.setColor("Color", splashSomeColorOnMyBrush());
                if (name.equals("shoot") && !keyPressed) {
                    Geometry bulletg = new Geometry("bullet", bullet);
                    bulletg.setMaterial(mat2);
                    bulletg.setShadowMode(ShadowMode.CastAndReceive);
                    bulletg.setLocalTranslation(app.getCamera().getLocation());
                    RigidBodyControl bulletControl = new RigidBodyControl(10);
                    bulletg.addControl(bulletControl);
                    bulletControl.setLinearVelocity(app.getCamera().getDirection().mult(25));
                    bulletg.addControl(bulletControl);
                    rootNode.attachChild(bulletg);
                    space.add(bulletControl);
                }
            }
        };
        app.getInputManager().addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(actionListener, "shoot");
    }

    /**
     * Create the wall around the 'floor' to entrap balls and characters
     *
     * @author Nick Radonic
     */
    public static void createWall(Node rootNode, AssetManager assetManager,
            PhysicsSpace space) {
        final float WallHeight = 20f;
        Vector3f wallSize = new Vector3f(WallHeight, WallHeight, 0.1f);
        Vector3f wallLocation = new Vector3f(0, WallHeight, -WallHeight);
        createFixedBoundaryWall(rootNode, assetManager, space, wallSize, wallLocation);

        // parameters for a wall 
        wallSize = new Vector3f(WallHeight, WallHeight, 0.1f);
        wallLocation = new Vector3f(0, WallHeight, WallHeight);
        createFixedBoundaryWall(rootNode, assetManager, space, wallSize, wallLocation);

        // parameters for a wall 
        wallSize = new Vector3f(0.1f, WallHeight, WallHeight);
        wallLocation = new Vector3f(-WallHeight, WallHeight, 0);
        createFixedBoundaryWall(rootNode, assetManager, space, wallSize, wallLocation);

        // parameters for a wall 
        wallSize = new Vector3f(0.1f, WallHeight, WallHeight);
        wallLocation = new Vector3f(WallHeight, WallHeight, 0);
        createFixedBoundaryWall(rootNode, assetManager, space, wallSize, wallLocation);
        
        // parameters for a roof 
        wallSize = new Vector3f(WallHeight, .1f, WallHeight);
        wallLocation = new Vector3f(0, 2 * WallHeight, 0);
        createFixedBoundaryWall(rootNode, assetManager, space, wallSize, wallLocation);
        
    }
}
