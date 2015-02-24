package mygame;

import appstate.InputAppState;
import characters.AICharacterControl;
import physics.PhysicsTestHelper;
import characters.MyGameCharacterControl;
import characters.NavMeshNavigationControl;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.infos.RigidBodyMotionState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * test - modified for CMSC 325 week homework
 *
 * @author normenhansen
 * @author Nick Radonic
 */
public class Main extends SimpleApplication {

    protected BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    private Node playerNode1;
    private ArrayList<Geometry> balls = new ArrayList<Geometry>();
    private ArrayList<BallState> ballRecords = new ArrayList<BallState>();
    private int sampleNumber = 1;
//    private Node playerNode2;
//    private Vector3f player2Vector = new Vector3f(0.1f, 0f, 0.1f);
    private BitmapText HUDText;
    private final String basicMessage = "CMSC325 Project2\n\n";
    private String str1Line = "";
    private Node aic;
    private Vector3f target;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        bulletAppState = new BulletAppState(); //Allows for the use of Physics simulation
        stateManager.attach(bulletAppState);
        // custom camera positioning:
        cam.setLocation(new Vector3f(0, 25, 100));
        Vector3f upV = new Vector3f(0, 0.95f, -0.3f);
        Vector3f leftV = new Vector3f(-1f, 0, 0);
        Vector3f dirV = new Vector3f(0, -0.3f, -.95f);
        cam.setAxes(leftV, upV, dirV);
        flyCam.setMoveSpeed(100f);

        //Add the Scene
        Node scene = setupWorld();
        setupCharacterScene(scene);
        //     rootNode.attachChild(scene);
        //stateManager.detach(stateManager.getState(FlyCamAppState.class));

        //Create the Physics World based on the Helper class
        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        balls = PhysicsTestHelper.balls; //static arraylist of ball geometries

        // parameters for a wall 
        // Nick Radonic
        PhysicsTestHelper.createWall(rootNode, assetManager, bulletAppState.getPhysicsSpace());

        //Add a custom font and text to the scene
        // Nick Radonic YuGothic font
        BitmapFont myFont = assetManager.loadFont("Interface/Fonts/YuGothic.fnt");

        BitmapText hudText = new BitmapText(myFont, true);
        hudText.setText(basicMessage);
        HUDText = hudText;  // NR reference connection

        hudText.setColor(ColorRGBA.Red);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());

        //Set the text in the middle of the screen
        //Positions text to middle of screen
        hudText.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2 + hudText.getLineHeight(), 0f);
        guiNode.attachChild(hudText);

        // Nick Radonic debug collisions
        bulletAppState.setDebugEnabled(true);


    }

    private Node setupWorld() {
        Node scene = (Node) assetManager.loadModel("Scenes/Week6Scene.j3o");
        rootNode.attachChild(scene);
        Geometry navGeom = new Geometry("NavMesh");
        navGeom.setMesh(((Geometry) scene.getChild("NavMesh")).getMesh());
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
        green.getAdditionalRenderState().setWireframe(true);
        navGeom.setMaterial(green);
        rootNode.attachChild(navGeom);

        Spatial terrain = scene.getChild("terrain-Week6Scene");
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().addAll(terrain);
        return scene;
    }

    private void setupCharacterScene(Node scene) {
        Node aiCharacter = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");

        AICharacterControl physicsCharacter = new AICharacterControl(0.3f, 2.5f, 8f);
        aiCharacter.addControl(physicsCharacter);
        aic = aiCharacter;

        bulletAppState.getPhysicsSpace().add(physicsCharacter);
        aiCharacter.setLocalTranslation(0, 10, 0);
        aiCharacter.setLocalScale(2f);
        scene.attachChild(aiCharacter);
        NavMeshNavigationControl navMesh = new NavMeshNavigationControl((Node) scene);

        aiCharacter.addControl(navMesh);

        // display search target location
        target = new Vector3f(60, 10, -55);
        navMesh.moveTo(target);

        Sphere sphereT = new Sphere(16, 16, 1);
        Geometry geom = new Geometry("Target", sphereT);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setLocalTranslation(target);
        geom.setMaterial(mat);
        geom.addControl(new RigidBodyControl(0));
        rootNode.attachChild(geom);

    }

    /**
     * Use this to display locaiton information and recenter beasts that have
     * dropped below the floor
     *
     * @param tpf
     * @author Nick Radonic
     */
    @Override
    public void simpleUpdate(float tpf) {
//        playerNode2.move(player2Vector);
        String str = basicMessage;
//        str += getPlayerInfo(playerNode1, "Player 1") + "\n";
//        str += getPlayerInfo(playerNode2, "Player 2");
        // str = CameraDiagnostics(str);
        boolean printStr = sampleNumber % 100 == 0;
        if (printStr) {
            str1Line = "";
        }
        //TODO: add update code
        for (int i = 0; i < balls.size(); i++) {
            Geometry x = balls.get(i);
            Vector3f xPLOC = x.getControl(RigidBodyControl.class).getPhysicsLocation();
            Vector3f xPVEL = x.getControl(RigidBodyControl.class).getLinearVelocity();
            Vector3f xROT = x.getControl(RigidBodyControl.class).getAngularVelocity();
            BallState bs = new BallState(i, sampleNumber, tpf, xPLOC, xPVEL, xROT);
            ballRecords.add(bs);
            if (printStr) {
                str1Line += bs.toString();
            }
        }
        sampleNumber++;
        str += str1Line;
        String targetS = aic.getLocalTranslation().toString() + "\n";
        targetS += target.toString();
        str += targetS;
        HUDText.setText(str);

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * puts together the HUD message
     *
     * @param player
     * @return position message
     * @author Nick Radonic
     */
    private String getPlayerInfo(Node player, String name) {
        Vector3f pos = player.getLocalTranslation();
        String result = "\n\n" + name + " x: " + pos.x + "\n" + name + " y: " + pos.y + "\n" + name + " z: " + pos.z;
        return result;
    }

    /**
     * Camera Diagnostics - reports camera positions and angles
     *
     * @param str
     * @return
     */
    private String CameraDiagnostics(String str) {
        Vector3f camDir = cam.getDirection();
        str += "\nCam Dir x: " + camDir.x + " y: " + camDir.y + " z:" + camDir.z + " z:" + "\n";
        Vector3f camLoc = cam.getLocation();
        str += "Cam Loc x: " + camLoc.x + " y: " + camLoc.y + " z:" + camLoc.z + "\n";
        Vector3f camUp = cam.getUp();
        str += "Cam Up x: " + camUp.x + " y: " + camUp.y + " z:" + camUp.z + "\n";
        Vector3f camLeft = cam.getLeft();
        str += "Cam Left x: " + camLeft.x + " y: " + camLeft.y + " z:" + camLeft.z + "\n";
        return str;
    }

    @Override
    public void stop() {
        String userHome = System.getProperty("user.home");
        try {
            //System.out.println("try");
            File file = new File("assets/savedgames/gameballs.txt");
            //System.out.println("new file "+file.getAbsolutePath());
            file.createNewFile();
            //System.out.println("create file");
            FileWriter fw = new FileWriter(file);
            //System.out.println("file writer");
            int samples = Math.min(500, ballRecords.size());
            for (int i = Math.max(0, ballRecords.size() - 500); i < ballRecords.size(); i++) {
                BallState bs = ballRecords.get(i);
                String bss = bs.toString();
                System.out.println(bss);
                fw.write(bss);
            }
            fw.flush();
            //System.out.println("flush fw");
            fw.close();
            //System.out.println("fw close");
            System.out.println(samples + " game ball position samples written to gameballs.txt");
        } catch (IOException ioex) {
            System.out.println("Can't save data file gameballs.txt\n" + ioex);
        }

        super.stop();
    }

    private Node createOttoCharacter() {
        //Add the Player to the world and use the customer character and input control classes
        float charHeight = 5f;
        Node dummyTranslation = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        Node playerNode = new Node();
        dummyTranslation.setLocalTranslation(new Vector3f(0, charHeight, 0));
        playerNode.attachChild(dummyTranslation);

        MyGameCharacterControl charControl = new MyGameCharacterControl(3f, charHeight, 80f);
        charControl.setCamera(cam);

        playerNode.addControl(charControl);
        charControl.setGravity(normalGravity);
        bulletAppState.getPhysicsSpace().add(charControl);
        InputAppState appState = new InputAppState();
        appState.setCharacter(charControl);
        stateManager.attach(appState);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        material.setColor("Color", ColorRGBA.Green);
        playerNode.setMaterial(material);
        return playerNode;
    }
}
