package game;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.scene.Node;


public class Main extends SimpleApplication implements ActionListener, PhysicsCollisionListener{

    private Bomber bomber;
    private Bomber bomber1;
    private boolean up = false, down = false, left = false, right = false;
    private boolean up1 = false, down1 = false, left1 = false, right1 = false;
    private BulletAppState state;
    private Node ducks = new Node();
    private int totalDucks = 0, totalItens=0;
    private Node dests = new Node();
            
    //0 -> Livre\
    //1 -> Bloco com corpo rígido
    //2 -> Bloco destrutivel
    //3 -> Bomber
    //4 -> Bomber1
    int mat[][] = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 3, 5, 2, 2, 2, 2, 2, 2, 1},
        {1, 5, 1, 2, 2, 2, 2, 1, 2, 1},
        {1, 2, 2, 1, 2, 2, 1, 2, 2, 1},
        {1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
        {1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
        {1, 2, 2, 1, 2, 2, 1, 2, 2, 1},
        {1, 2, 1, 2, 2, 2, 2, 1, 5, 1},
        {1, 2, 2, 2, 2, 2, 2, 5, 4, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();

    }
    

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 25f, 0));
        Quaternion PITCH090 = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(1,0,0));
        cam.setRotation(PITCH090);
        rootNode.attachChild(ducks);
        criarFisica();
        criarPiso();
        criarLuz();
        criarParede();
        initKeys();        
    }

    @Override
    public void simpleUpdate(float tpf) {
        bomber.upDateKeys(tpf, up, down, left, right);
        bomber1.upDateKeys(tpf, up1, down1, left1, right1);
        
        for(Spatial d : ducks.getChildren())
            d.rotate(0, tpf, 0);
    }

    @Override
    public void simpleRender(RenderManager rm) {
   
    }

    private void criarPiso() {

        Box boxMesh = new Box(mat.length, 0.1f, mat[0].length);
        Geometry boxGeo = new Geometry("A Textured Box", boxMesh);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/tex/Chao.jpg");
        boxMat.setTexture("ColorMap", monkeyTex);
        boxGeo.setMaterial(boxMat);
        rootNode.attachChild(boxGeo);

        RigidBodyControl r = new RigidBodyControl(0);
        boxGeo.addControl(r);

        state.getPhysicsSpace().add(r);

    }

    private void criarLuz() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-10.5f, -15f, -10.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(10.5f, -15f, 10.5f)).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2);

    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        switch (binding) {
            case "CharLeft":
                if (value) {
                    left = true;
                } else {
                    left = false;
                }
                break;
            case "CharRight":
                if (value) {
                    right = true;
                } else {
                    right = false;
                }
                break;
        }
        switch (binding) {
            case "CharForward":
                if (value) {
                    up = true;
                } else {
                    up = false;
                }
                break;
            case "CharBackward":
                if (value) {
                    down = true;
                } else {
                    down = false;
                }
                break;
        }
        
        switch (binding) {
            case "Char1Left":
                if (value) {
                    left1 = true;
                } else {
                    left1 = false;
                }
                break;
            case "Char1Right":
                if (value) {
                    right1 = true;
                } else {
                    right1 = false;
                }
                break;
        }
        switch (binding) {
            case "Char1Forward":
                if (value) {
                    up1 = true;
                } else {
                    up1 = false;
                }
                break;
            case "Char1Backward":
                if (value) {
                    down1= true;
                } else {
                    down1 = false;
                }
                break;
        }

    }

    private void createPlayer(Vector3f posicao, Vector3f posicao1) {
        if(posicao != null)            
        {
            bomber = new Bomber("bomber", assetManager, state,posicao);
            rootNode.attachChild(bomber);
        }
        if(posicao1 != null)
        {
            bomber1 = new Bomber("bomber1", assetManager, state,posicao1);
            Quaternion ROLL180  = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(1,0,0));
            bomber1.rotate(ROLL180);
            rootNode.attachChild(bomber1);

        }
        flyCam.setEnabled(false);
    }

    private void criarFisica() {
        state = new BulletAppState();
        stateManager.attach(state);
        state.getPhysicsSpace().addCollisionListener(this);
    }

    private void initKeys() {
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CharBackward", new KeyTrigger(KeyInput.KEY_S));

        inputManager.addListener(this, "CharLeft", "CharRight");
        inputManager.addListener(this, "CharForward", "CharBackward");
        
        inputManager.addMapping("Char1Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Char1Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Char1Forward", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Char1Backward", new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addListener(this, "Char1Left", "Char1Right");
        inputManager.addListener(this, "Char1Forward", "Char1Backward");
    }

    private void criarParede() {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {

                if (mat[i][j] == 1) {
                    Box boxMesh = new Box(1f, 1f, 1f);
                    Geometry boxGeo = new Geometry("Bloco", boxMesh);
                    Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    Texture monkeyTex = assetManager.loadTexture("Textures/tex/tijolo.jpg");
                    boxMat.setTexture("ColorMap", monkeyTex);
                    boxGeo.setMaterial(boxMat);
                    rootNode.attachChild(boxGeo);

                    RigidBodyControl r = new RigidBodyControl(0);
                    boxGeo.addControl(r);

                    boxGeo.move(-9+i*2,1,-9+j*2);
                    r.setPhysicsLocation(boxGeo.getLocalTranslation());

                    state.getPhysicsSpace().add(r);
                }
                else
                {
                    if(mat[i][j] == 2){
                        Box boxMesh2 = new Box(1f, 1f, 1f);
                        Material boxMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                        Geometry boxGeo2 = new Geometry("Bloco", boxMesh2);
                        Texture dest = assetManager.loadTexture("Textures/tex/box.jpg");
                        dest.setName("box");
                        boxMat2.setTexture("ColorMap", dest);
                        boxGeo2.setMaterial(boxMat2);
                        rootNode.attachChild(boxGeo2);
                       
                        RigidBodyControl r2 = new RigidBodyControl(0);
                        boxGeo2.addControl(r2);
                        
                        boxGeo2.move(-9+i*2,1,-9+j*2);
                        r2.setPhysicsLocation(boxGeo2.getLocalTranslation());
                        
                        state.getPhysicsSpace().add(r2);
                    }
                    if(mat[i][j]==3){
                       createPlayer(new Vector3f(-9+i*2,1,-9+j*2),null);
                    }
                    if(mat[i][j]==4){
                        createPlayer(null,new Vector3f(-9+i*2,1,-9+j*2));
                     }
                }
            }
        }
        

    }

    private void createDuck(Vector3f posicao) {
        Spatial duck = assetManager.loadModel("Models/fuck/Duck.gltf");
        duck.setName("duck");
        duck.setLocalTranslation(posicao);
        Material defaultMat = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        duck.setMaterial(defaultMat);
        ducks.attachChild(duck);
        
        RigidBodyControl r = new RigidBodyControl(0);
        duck.addControl(r);
        state.getPhysicsSpace().add(r);
    }
    @Override
    public void collision(PhysicsCollisionEvent event) {
        Spatial nodeA = event.getNodeA();
        Spatial nodeB = event.getNodeB();
              
        if(nodeA.getName().equals("duck"))
        {
            if(ducks.getChildIndex(nodeA) != -1){
             state.getPhysicsSpace().remove(nodeA);
             ducks.detachChild(nodeA);
             totalItens++;
            
            }
        }
        else{
            if(nodeB.getName().equals("duck")){
                if(ducks.getChildIndex(nodeB) != -1){
                state.getPhysicsSpace().remove(nodeB);
                ducks.detachChild(nodeB);
                totalItens++;
                }
            }
        }
    }
}
