package game;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
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

/**
 *
 * @author GabrielEzidio
 */
public class Bomb extends Node{
    public Bomb(String name, AssetManager assetManager, BulletAppState bulletAppState, Vector3f posicao)
    {
        super(name);
        Spatial bomb = assetManager.loadModel("Models/fuck/Duck.gltf");
        bomb.setName("bomb");
        bomb.setLocalTranslation(posicao);
        Material defaultMat = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        bomb.setMaterial(defaultMat);
        //bombs.attachChild(bomb);
        
        RigidBodyControl r = new RigidBodyControl(0);
        bomb.addControl(r);
        bulletAppState.getPhysicsSpace().add(r);

    }
}