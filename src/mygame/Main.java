package mygame;

import appstate.InputAppState;
import physics.PhysicsTestHelper;
import characters.MyGameCharacterControl;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.awt.Color;

/**
 * test - modified for CMSC 325 week homework
 * @author normenhansen
 * @author Nick Radonic 
 */
public class Main extends SimpleApplication {

    protected BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    private Node playerNode1;
    
//    private Node playerNode2;
    public Boolean currentControl = new Boolean(true);
    
//    private Vector3f player2Vector = new Vector3f(0.1f, 0f, 0.1f);
   
    private BitmapText HUDText;
    private final String basicMessage = "CMSC325 Week3 Physics Intro!\n\n\t\t+";
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // custom camera positioning:
        cam.setLocation(new Vector3f(0,25,50));
        Vector3f upV = new Vector3f(0, 0.95f, -0.3f);
        Vector3f leftV =  new Vector3f(-1f, 0, 0);
        Vector3f dirV = new Vector3f(0, -0.3f, -.95f);
        cam.setAxes(leftV, upV, dirV);
        
        //Add the Scene
        Spatial scene = assetManager.loadModel("Scenes/Week3Scene.j3o");
        rootNode.attachChild(scene);
        
        bulletAppState = new BulletAppState(); //Allows for the use of Physics simulation
        stateManager.attach(bulletAppState);
        //stateManager.detach(stateManager.getState(FlyCamAppState.class));
        
        //Create the Physics World based on the Helper class
        PhysicsTestHelper.createPhysicsTestWorldSoccer(rootNode, assetManager, bulletAppState.getPhysicsSpace());
        
        // parameters for a wall 
        // Nick Radonic
        PhysicsTestHelper.createWall(rootNode, assetManager, bulletAppState.getPhysicsSpace());

        //Add the Player to the world and use the customer character and input control classes
        Node playerNode = (Node)assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        playerNode1 = playerNode;
        MyGameCharacterControl charControl = new MyGameCharacterControl(3f,8f,80f);
        
        charControl.setCamera(cam);
        charControl.setNodes(playerNode1, currentControl, true); // modified control param
                
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
        
        rootNode.attachChild(playerNode);
        currentControl = true;
        
        // Nick Radonic add second character
        //Add the Player to the world and use the customer character and input control classes
//        playerNode2 = (Node)assetManager.loadModel("Models/Oto/Oto.mesh.xml");
//        MyGameCharacterControl charControl2 = new MyGameCharacterControl(4f,8f,20f);
//        charControl2.setNodes(playerNode2, currentControl, false);
//        
//        Vector3f P2Location = new Vector3f(-10f, 0, 13f);
//        playerNode2.setLocalTranslation(P2Location);
//        //playerNode2.
//        //cam.setLocation(new Vector3f(5,25,25));
//        charControl2.setCamera(cam);
//        playerNode2.addControl(charControl2);
//        charControl2.setGravity(normalGravity);
//        bulletAppState.getPhysicsSpace().add(charControl2);
//        
//        InputAppState appState2 = new InputAppState();
//        appState2.setCharacter(charControl2);
//        stateManager.attach(appState2);
//        rootNode.attachChild(playerNode2);
//        
        
        flyCam.setMoveSpeed(100f);
        //Add the "bullets" to the scene to allow the player to shoot the balls
        PhysicsTestHelper.createBallShooter(this,rootNode,bulletAppState.getPhysicsSpace());
        
        //Add a custom font and text to the scene
        // Nick Radonic YuGothic font
         BitmapFont myFont = assetManager.loadFont("Interface/Fonts/YuGothic.fnt");

        BitmapText hudText = new BitmapText(myFont, true);
        hudText.setText(basicMessage);
        HUDText = hudText;  // NR reference connection
        
        hudText.setColor(ColorRGBA.Red);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        
        //Set the text in the middle of the screen
        hudText.setLocalTranslation(settings.getWidth() /2 , settings.getHeight() / 2 + hudText.getLineHeight(), 0f); //Positions text to middle of screen
        guiNode.attachChild(hudText);
        
        // Nick Radonic debug collisions
        bulletAppState.setDebugEnabled(false);
        
    }

    /**
     * Use this to display locaiton information and recenter beasts that have dropped below the floor
     * @param tpf 
     * @author Nick Radonic
     */
    @Override
    public void simpleUpdate(float tpf) {
//        playerNode2.move(player2Vector);
        String str =  basicMessage;
        str += getPlayerInfo(playerNode1, "Player 1");
//        str += getPlayerInfo(playerNode2, "Player 2");
       // str = CameraDiagnostics(str);
        
        
        HUDText.setText(str);
         
        keepPlayerOnSurface(playerNode1);
//        keepPlayerOnSurface(playerNode2);
        
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
   /**
    * puts together the HUD message
    * @param player
    * @return position message 
    * @author Nick Radonic
    */ 
    private String getPlayerInfo(Node player, String name){
         Vector3f pos = player.getLocalTranslation();
         String result = "\n\n"+name+" x: "+pos.x+"\n"+name+" y: "+pos.y+"\n"+name+" z: "+pos.z;
         return result;
    }
    
    /**
     * lifts a player's location above the surface if it falls
     * @param player 
     * @author Nick Radonic
     */
    private void keepPlayerOnSurface(Node player){
        
        Vector3f posn = player.getLocalTranslation();
        if (posn.y < 0){
            posn.y = 100;
            player.setLocalTranslation(posn);
        }
    }

    /**
     * Camera Diagnostics - reports camera positions and angles
     * @param str
     * @return 
     */
    private String CameraDiagnostics(String str) {
        Vector3f camDir = cam.getDirection();
        str += "\nCam Dir x: "+camDir.x+" y: "+camDir.y+" z:"+camDir.z+" z:"+"\n";
        Vector3f camLoc = cam.getLocation();
        str += "Cam Loc x: "+camLoc.x+" y: "+camLoc.y+" z:"+camLoc.z+"\n";
        Vector3f camUp = cam.getUp();
        str += "Cam Up x: "+camUp.x+" y: "+camUp.y+" z:"+camUp.z+"\n";
        Vector3f camLeft = cam.getLeft();
        str += "Cam Left x: "+camLeft.x+" y: "+camLeft.y+" z:"+camLeft.z+"\n";
        return str;
    }
    
}
