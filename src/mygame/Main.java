package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.WaterFilter;

public class Main extends SimpleApplication{

    public static Node s_TreeNode;
    public static AssetManager s_AssetManager;
    public static AppStateManager s_StateManager;
    public static InputManager s_InputManager;
    
    private PlayerNode player;
    public static BulletAppState bulletAppState;
    
    private Scene mainScene;
    private Sun sun;
    
    BitmapText hudText;
    public static Inventory inventory;
    Rect testRect;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        testRect = new Rect(50, 50, 200, 200);
        
        s_TreeNode = new Node();
        s_AssetManager = assetManager;
        s_StateManager = stateManager;
        s_InputManager = inputManager;
       
        ObjectHelper.LoadObjectHelper();
        ItemInformation.InitItemInfo();
        
        mouseInput.setCursorVisible(true);
        flyCam.setEnabled(false);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        mainScene = new Scene();
        mainScene.initScene(rootNode, viewPort);

        sun = new Sun();
        sun.initSun(rootNode, viewPort, cam);
        inventory = new Inventory(this, guiNode, rootNode, cam, mainScene.sceneModel);
        Spatial mushroom = assetManager.loadModel("Models/MushroomAnim2/mushroom.mesh.xml");
        mushroom.rotate(0, 77, 0);
        player = new PlayerNode(mushroom, inputManager, cam, mainScene.sceneModel);
        player.getCharacterControl().setPhysicsLocation(new Vector3f(0, 4f, 0));
        rootNode.attachChild(player);
        bulletAppState.getPhysicsSpace().add(player);
        
        Spatial model =  ObjectHelper.AddModel(new Vector3f(5, 0, 5), "tree");
        Spatial model1 = ObjectHelper.AddModel(new Vector3f(5, 0, 8), "rock");
        s_TreeNode.attachChild(model1);
        s_TreeNode.attachChild(model);
        
                          
        rootNode.attachChild(s_TreeNode);

        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setColor(ColorRGBA.White);
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0);
        guiNode.attachChild(hudText);
        

        /*Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1, 0, 0, 0.5f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        for(int i = 0; i < 40; i++){
            Geometry geo = new Geometry("Rect", new Quad(38, 38));
            geo.setMaterial(mat);
            geo.setLocalTranslation(new Vector3f(inventory.inventorySlots[i].rect.x, inventory.inventorySlots[i].rect.y, 0));
            guiNode.attachChild(geo);
        }*/
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
        //ObjectHelper.HighlightModel(cam, inputManager);
        ObjectHelper.MoveModel(cam, inputManager, mainScene.sceneModel);
        sun.updateSun(tpf);
        hudText.setText(sun.timeOfDay.getHour() + ":" + sun.timeOfDay.getSecond());
        inventory.simpleUpdate(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
