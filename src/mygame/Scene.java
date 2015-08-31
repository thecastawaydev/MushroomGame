/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import static mygame.Main.bulletAppState;

/**
 *
 * @author christian
 */
public class Scene {
    Node sceneNode;
    public Spatial sceneModel;
    private FilterPostProcessor fpp;
    private WaterFilter water;
    private float waterHeight = -10f;
    
    public void initScene(Node rootNode, ViewPort viewport)
    {
        sceneNode = new Node();
        sceneModel = Main.s_AssetManager.loadModel("Scenes/newScene.j3o");
        sceneModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);
        
        /*fpp = new FilterPostProcessor(Main.s_AssetManager);
        water = new WaterFilter(rootNode, new Vector3f(-5, -1, 6));
        water.setWaterHeight(waterHeight);
        fpp.addFilter(water);
        viewport.addProcessor(fpp);*/
  
    }
}
