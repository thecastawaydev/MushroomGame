/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
/**
 *
 * @author christian
 */
public class PlayerNode extends Node implements ActionListener, AnalogListener, AnimEventListener{
    boolean isClicked = false;
    private AnimChannel channel;
    private AnimControl control;
    
    private ThirdPersonCamera camera;
    private Camera cam;
    private Spatial sceneModel;
    
    private Spatial model;
    private CharacterControl characterControl;
    private InputManager inputManager;
    private Vector3f walkDirection = new Vector3f();
    
    private boolean left = false, right = false, up = false, down = false;
    private boolean lookLeft = false, lookRight = false, lookDown = false, lookUp = false;
    private boolean zoomIn = false, zoomOut = false;
    private boolean mmLeft, mmRight, mmUp, mmDown, mmSetModel;
    
    private float walkSpeed = .15f;
    private float stepSize = .05f;
    
    public PlayerNode( Spatial model, InputManager inputManager, Camera cam, Spatial sceneModel)
    {
        super();
        this.cam = cam;
        camera = new ThirdPersonCamera("CamNode", cam, this);
        this.sceneModel = sceneModel;
        this.model = model;
        this.model.scale(0.2f);
        this.model.setLocalTranslation(0, -1f, 0);
        this.model.setShadowMode(ShadowMode.CastAndReceive);
        this.attachChild(this.model);
        
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(0.5f, 1f);
        
        characterControl = new CharacterControl(playerShape, stepSize);
        this.addControl(characterControl);
        
        this.inputManager = inputManager;
        setUpKeys();
        
        control = new AnimControl();
        control.addListener(this);
        channel = control.createChannel();
    }
    
    public void update(float tpf)
    {
        Vector3f camDir = cam.getDirection().clone();
        camDir.y = 0;
        Vector3f camLeft = cam.getLeft().clone();
        camLeft.y = 0;
        walkDirection.set(0, 0, 0);
        
        if(left){
            walkDirection.addLocal(camLeft);
        }
        if(right){
            walkDirection.addLocal(camLeft.negate());
        }
        if(up){
            walkDirection.addLocal(camDir);
        }
        if(down){
            walkDirection.addLocal(camDir.negate());
        }   
        
        if(lookUp){
            camera.verticalRotate(FastMath.PI * tpf);
        }
        if(lookDown){
            camera.verticalRotate(-FastMath.PI * tpf);
        }
        if(lookRight){
            Quaternion turn = new Quaternion();
            turn.fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            characterControl.setViewDirection(turn.mult(characterControl.getViewDirection()));
        }
        if(lookLeft){
            Quaternion turn = new Quaternion();
            turn.fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            characterControl.setViewDirection(turn.mult(characterControl.getViewDirection()));
        }
        
        if(mmLeft){
            
            ObjectHelper.SetModel(new Vector3f(ObjectHelper.modelX--, 0f, ObjectHelper.modelZ));
        }
        if(mmRight){
             ObjectHelper.SetModel(new Vector3f(ObjectHelper.modelX++, 0f, ObjectHelper.modelZ));
        }
        if(mmUp){
             ObjectHelper.SetModel(new Vector3f(ObjectHelper.modelX, 0f, ObjectHelper.modelZ--));
        }
        if(mmDown){
             ObjectHelper.SetModel(new Vector3f(ObjectHelper.modelX, 0f, ObjectHelper.modelZ++));
        }
        characterControl.setWalkDirection(walkDirection.normalize().multLocal(walkSpeed));
    }
    
    private void setUpKeys()
    {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("TurnLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("TurnRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("LookDown", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("LookUp", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("ZoomIn", new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("ZoomOut", new KeyTrigger(KeyInput.KEY_SUBTRACT));
        inputManager.addMapping("LeftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("SetModel", new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "TurnLeft");
        inputManager.addListener(this, "TurnRight");
        inputManager.addListener(this, "LookDown");
        inputManager.addListener(this, "LookUp");
        inputManager.addListener(this, "ZoomIn");
        inputManager.addListener(this, "ZoomOut");
        inputManager.addListener(this, "LeftClick");
        inputManager.addListener(this, "RightClick");
        inputManager.addListener(this, "SetModel");
    }   

    public void onAnalog(String binding, float intensity, float tpf)
    {

        /*if(binding.equals("LeftClick")){
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
            Ray ray = new Ray(click3d, dir);
            Main.s_TreeNode.collideWith(ray, results);
            
            if(results.size() > 0){
                Spatial target = results.getClosestCollision().getGeometry(); 
                     InventoryHelper.AddToInventory(target, 1);
                ObjectHelper.RemoveModel(target);
            }
            //isClicked = true;
            
           
        }
        
        
        /*if(binding.equals("RightClick")){
             CollisionResults results = new CollisionResults();
             Ray ray = new Ray();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d).normalizeLocal();
            ray.setOrigin(click3d);
            ray.setDirection(dir);
             ray = new Ray(click3d, dir);
            sceneModel.collideWith(ray, results);
            
            if(results.size() > 0){
                if(Statics.s_PlayerSettingModel == false){
                
                CollisionResult point = results.getClosestCollision();
                Vector3f destination = point.getContactPoint();

                ObjectHelper.s_Model = ObjectHelper.AddModel(destination, "tree");
                
                Main.s_TreeNode.attachChild(ObjectHelper.s_Model);
                Statics.s_PlayerSettingModel = true;
                }
            }
        }*/
       
    }

    public void onAction(String binding, boolean value, float tpf){
        if(binding.equals("Left")){
            left = value;
        }
        else if(binding.equals("Right")){
            right = value;
        }
        else if(binding.equals("Up")){
            up = value;
            channel.setAnim("my_animation", 0.5f);
            channel.setLoopMode(LoopMode.Loop);
        }
        else if(binding.equals("Down")){
            down = value;
        }
        else if(binding.equals("TurnRight")){
            lookRight = value;
        }
        else if(binding.equals("TurnLeft")){
            lookLeft = value;
        }
        else if(binding.equals("LookDown")){
            lookDown = value;
        }
        else if(binding.equals("LookUp")){
            lookUp = value;
        }
        else if(binding.equals("ZoomIn")){
            zoomIn = value;
        }
        else if(binding.equals("ZoomOut")){
            zoomOut = value;
        }

        else if(binding.equals("SetModel")){
            if(Statics.s_PlayerSettingModel == true){
                Statics.s_PlayerSettingModel = false;
            }
        }
        
        if(binding.equals("LeftClick") && !value){
            isClicked = true;
            if(isClicked){
                ObjectHelper.damageObject(cam, inputManager, sceneModel);
                isClicked = false;
            }
        }
    }

    public CharacterControl getCharacterControl()
    {
        return characterControl;
    }
    public ThirdPersonCamera getCameraNode(){
        return camera;
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
