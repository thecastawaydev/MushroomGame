/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author christian
 */
public final class ObjectHelper {
    
    private static RigidBodyControl physicsControl;
    public static Spatial s_Model;
    public static float modelX;
    public static float modelZ;
    public static Material highlightMat, material;
    public static Material greenTrans, redTrans;
    
    public static void LoadObjectHelper(){
        highlightMat = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = Main.s_AssetManager.loadTexture("Models/tree.png");
        highlightMat.setTexture("ColorMap", texture);
        
        greenTrans = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        greenTrans.setColor("Color", new ColorRGBA(0, 1, 0, 0.3f));
        greenTrans.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        redTrans = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redTrans.setColor("Color", new ColorRGBA(1, 0, 0, 0.3f));
        redTrans.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
    public static Spatial AddModel(Vector3f position, String name){
            s_Model = Main.s_AssetManager.loadModel("Models/" + name +"/" + name + ".mesh.xml");
            //s_Model = Main.s_AssetManager.loadModel("Models/" + name + ".obj");
            
            s_Model.setLocalTranslation(position);
            s_Model.scale(ItemInformation.table.get(name).getScale());
            s_Model.setName(name);
            s_Model.setUserData("health", ItemInformation.table.get(name).getHealth());
            s_Model.setShadowMode(ShadowMode.CastAndReceive);
            
            CollisionShape collision = ItemInformation.table.get(name).getCollision();        
            physicsControl = new RigidBodyControl(collision, 0);
            physicsControl.setKinematic(true);
            s_Model.addControl(physicsControl);
            Main.bulletAppState.getPhysicsSpace().add(s_Model);

            return s_Model;
    }
    
    public static void SetModel(Vector3f position){
        s_Model.setLocalTranslation(position);
        s_Model.getControl(RigidBodyControl.class).setPhysicsLocation(position);
    }
    
    public static void RemoveModel(Spatial model)
    {
        Main.bulletAppState.getPhysicsSpace().remove(model.getControl(RigidBodyControl.class));
        //Node node = model.getParent();
        //Main.bulletAppState.getPhysicsSpace().remove(node);
        model.removeFromParent();     
    }
    
    
    public static void MoveModel(Camera cam, InputManager inputManager, Spatial scene)
    {
        if(Statics.s_PlayerSettingModel == true){
            CollisionResults results = new CollisionResults();
            Ray ray = new Ray();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d).normalizeLocal();
            ray.setOrigin(click3d);
            ray.setDirection(dir);
             ray = new Ray(click3d, dir);
             scene.collideWith(ray, results);
            
             if(results.size() > 0){
                 CollisionResult point = results.getClosestCollision();
                Vector3f destination = point.getContactPoint();
                SetModel(destination);
             }
        }
    }
    
    public static void damageObject(Camera cam, InputManager inputManager, Spatial scene){
        CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
            Ray ray = new Ray(click3d, dir);
            Main.s_TreeNode.collideWith(ray, results);
            
            int prev = 0;
            
            if(results.size() > 0){
                            
                Spatial target = results.getClosestCollision().getGeometry().getParent(); 
                     prev = target.getUserData("health");
                    prev -= 10;
                    target.setUserData("health", prev);
                    System.out.println(target.getUserData("health"));
                    if((Integer)target.getUserData("health") <= 0){
                        RemoveModel(target);
                        InventoryHelper.AddToInventory(target, 5);
                    }
            }
    }
    
    private static void fadeOutModel(Spatial  target){
       
        Geometry geo = (Geometry)target.getParent().getChild(target.getName());
        Material mat = geo.getMaterial();
        mat.setTransparent(true);
    }
  
}
