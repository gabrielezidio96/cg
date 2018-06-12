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
    private boolean up = false, down = false, left = false, right = false;
    private BulletAppState state;
    private Node ducks = new Node();
    private int totalDucks = 0, totalItens=0;
    private Node dests = new Node();
            
    //0 -> Livre\
    //1 -> Bloco com corpo rígido
    //2 -> Bloco destrutivel
    //3 -> Bomber
    //4 -> Bomber2
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

    }

    private void createPlayer(Vector3f posicao) {
        bomber = new Bomber("bomber", assetManager, state,posicao);
        rootNode.attachChild(bomber);
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
                       createPlayer(new Vector3f(-9+i*2,1,-9+j*2));
                    }
                   //if(mat[i][j]==4){
                   //    createPlayer2(new Vector3f(-9+i*2,1,-9+j*2));
                   // }
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
