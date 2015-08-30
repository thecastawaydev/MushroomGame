package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.DragElement;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.core.layouts.LayoutHelper;
import tonegod.gui.core.utils.UIDUtil;

public class Inventory extends SimpleApplication implements RawInputListener, ActionListener, AnalogListener {
    
public Screen screen;
private float iconSize = 40;
Vector2f dim = new Vector2f();
Vector4f windowPadding = new Vector4f();
float dragBarHeight;
private Node rootNode;

Vector2f click2d = new Vector2f(), tempV2 = new Vector2f();
Vector3f click3d = new Vector3f(), pickDir = new Vector3f(), tempV3 = new Vector3f();
Ray pickRay = new Ray();
CollisionResults rayResults = new CollisionResults();
CollisionResult closest;
Camera cam;

boolean showInventory = false;
public Element inventory;
Window win;
InventorySlot[] inventorySlots;
ButtonAdapter[] inventoryElements;

Element mouseFocusElement;
Vector3f guiRayOrigin = new Vector3f();
Ray elementZOrderRay = new Ray();
CollisionResult lastCollision;
Boolean mousePressed = false;
float eventElementOffsetX = 0;
float eventElementOffsetY = 0;
Element contactElement = null;
Spatial model;

public Inventory(Main main, Node guiNode, Node rootNode, Camera cam, Spatial model){
    initInventory(main, guiNode, rootNode, cam, model);
}
public void initInventory(Main main, Node guiNode, Node rootNode, Camera cam, Spatial model) {

     inventorySlots = new InventorySlot[40];
     inventoryElements = new ButtonAdapter[40];
	createGUIScreen(main, guiNode);
	layoutGUI();

        this.rootNode = rootNode;
        this.cam = cam;
        this.model = model;
                
            Main.s_InputManager.addMapping("ShowInventory", new KeyTrigger(KeyInput.KEY_I));
    Main.s_InputManager.addListener(this, "ShowInventory");
    Main.s_InputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    Main.s_InputManager.addListener(this, "RightClick");
    
   
}


private void createGUIScreen(Main main, Node guiNode) {
	screen = new Screen(main);
	screen.setUseUIAudio(true);
	screen.setUIAudioVolume(1f);
        screen.setUseToolTips(true);
        screen.setGlobalAlpha(0.4f);
	guiNode.addControl(screen);

}

private void layoutGUI() {
	windowPadding.set(screen.getStyle("Window#Dragbar").getVector4f("indents"));
	dragBarHeight = screen.getStyle("Window#Dragbar").getFloat("defaultControlSize");

	 inventory = new Element(
		screen,
		UIDUtil.getUID(),
		new Vector2f(
			windowPadding.x,
			(windowPadding.y*2)+dragBarHeight
		),
		Vector2f.ZERO,
		Vector4f.ZERO,
		null
	);
        
	inventory.setAsContainerOnly();
        
	for (int i = 0; i < 40; i++) {
            
		float x = i *iconSize;
                float y = 0;
		x += 2.5f;
                
                if(i >= 10 && i <= 19){
                    y = 1 * iconSize;
                    x = (i - 10) * iconSize + 2.5f;
                }
                else if(i >= 20 && i <= 29){
                    y = 2 * iconSize;
                    x = (i - 20) * iconSize + 2.5f;
                 }
                else if(i >= 30 && i <= 39){
                    y = 3 * iconSize;
                    x = (i - 30) * iconSize + 2.5f;
                }
                 ButtonAdapter e = createButtonSlot(i, x, y); 
                 
                inventoryElements[i] = e;
                inventorySlots[i] = new InventorySlot(i, 0, "empty");
                e.setToolTipText(inventorySlots[i].itemName + " : " + inventorySlots[i].slotNumber + " : " + inventorySlots[i].quantity);
		inventory.addChild(e);
                
	}

	inventory.sizeToContent();

	dim.set(
		inventory.getWidth()+(windowPadding.x*2),
		inventory.getHeight()+(windowPadding.y*3)+dragBarHeight
	);

	 win = new Window(screen, Vector2f.ZERO, dim);
	win.addChild(inventory);
	win.setLockToParentBounds(true);
	win.setIsResizable(false);
	screen.addElement(win);
}
private ButtonAdapter createButtonSlot(int index, float x, float y){
       ButtonAdapter slot = new ButtonAdapter(
            screen, 
            "InvSlot" + index,
            new Vector2f(x, y),
            LayoutHelper.dimensions(40, 40),
            screen.getStyle("CheckBox").getVector4f("resizeBorders"),
            screen.getStyle("CheckBox").getString("defaultImg")
            ){
                @Override
                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled){
                        int index = this.getUserData("Index");
                        //if(inventorySlots[index].slotNumber == this.getUserData("Index"))
                            if(inventorySlots[index].itemName != "empty"){
                                Statics.s_ShowInventory = false;
                                CollisionResults results = new CollisionResults();
             Ray ray = new Ray();
            Vector2f click2d = Main.s_InputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d).normalizeLocal();
            ray.setOrigin(click3d);
            ray.setDirection(dir);
             ray = new Ray(click3d, dir);
            model.collideWith(ray, results);
            
            if(results.size() > 0){
                if(Statics.s_PlayerSettingModel == false){
                
                CollisionResult point = results.getClosestCollision();
                Vector3f destination = point.getContactPoint();

                 ObjectHelper.s_Model = ObjectHelper.AddModel(destination, inventorySlots[index].itemName);
                
                Main.s_TreeNode.attachChild(ObjectHelper.s_Model);
                inventorySlots[index].quantity--;
                inventoryElements[index].setToolTipText(inventorySlots[index].itemName + " : " + inventorySlots[index].slotNumber + " : " + inventorySlots[index].quantity);
                if(inventorySlots[index].quantity <= 0){
                    inventorySlots[index].itemName = "empty";
                    inventoryElements[index].setToolTipText(inventorySlots[index].itemName + " : " + inventorySlots[index].slotNumber + " : " + inventorySlots[index].quantity);
                    inventoryElements[index].setButtonIcon(0, 0, "Textures/blank.png");
                    
                }
                Statics.s_PlayerSettingModel = true;
                           
                            
                }
                }
                            }
                    }
                
            };
    slot.clearAltImages();
    slot.setUserData("Index", index);
    slot.setEffectZOrder(false);
    slot.setScaleEW(false);
    slot.setScaleNS(false);
    slot.setDocking(Element.Docking.SW);
    //slot.setButtonIcon(0, 0, "Textures/blank.png");
    slot.setIsDragDropDragElement(true);
    return slot;
    
}


public DragElement createNewDragElement() {
	DragElement e = new DragElement(
		screen,
		new Vector2f(
			screen.getMouseXY().x-(iconSize/2),
			screen.getHeight()-(screen.getMouseXY().y+(iconSize/2))

		),
		new Vector2f(iconSize,iconSize),
		Vector4f.ZERO,
		"tonegod/gui/style/def/Common/Particles/spark.png"
	) {
		@Override
		public void onDragStart(MouseButtonEvent evt) {

		}
		@Override
		public boolean onDragEnd(MouseButtonEvent evt, Element dropElement) {
			if (dropElement != null) {
				setlockToParentBounds(false);
				return true;
			} else {
				Node n = getUserData("worldObject");
				rootNode.attachChild(n);
				screen.removeElement(this);
				return false;
			}
		}
	};
	e.setlockToParentBounds(true);
	e.setUseLockToDropElementCenter(true);
	e.setUseSpringBack(true);
	screen.addElement(e);
	return e;
}



public void beginInput() {  }
public void endInput() {  }
public void onJoyAxisEvent(JoyAxisEvent evt) {  }
public void onJoyButtonEvent(JoyButtonEvent evt) {  }
public void onMouseMotionEvent(MouseMotionEvent evt) {
}
public void onMouseButtonEvent(MouseButtonEvent evt) {}
public void onKeyEvent(KeyInputEvent evt) {  }
public void onTouchEvent(TouchEvent evt) {  }
public void onAction(String name, boolean value, float tpf) {
         if(name.equals("ShowInventory")){
            if(!value){
                if(Statics.s_ShowInventory == true){
                    Statics.s_ShowInventory = false;
                }else{
                    Statics.s_ShowInventory = true;
                }
            }
        }
    }
    
    @Override
    public void simpleInitApp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void simpleUpdate(float tpf) {

    if(Statics.s_ShowInventory == true)
       win.show();
    else
      win.hide();
}

    public void onAnalog(String name, float value, float tpf) {
   
    }
}
