/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.awt.Rectangle;

/**
 *
 * @author christian
 */
public class InventoryHelper {

    public static void AddToInventory(Spatial itemToAdd, int quantityToAdd){
        for(int i = 0; i < 40; i++){
            if(Main.inventory.inventorySlots[i].itemName.equals(itemToAdd.getName())){
                Main.inventory.inventorySlots[i].quantity += 1;
                Main.inventory.inventoryElements[i].setToolTipText(itemToAdd.getName() +  " : " + Main.inventory.inventorySlots[i].slotNumber + " : " + Main.inventory.inventorySlots[i].quantity);
                break;
            }
            else if(Main.inventory.inventorySlots[i].itemName.equals("empty")){
                Main.inventory.inventorySlots[i].setName(itemToAdd.getName());
                Main.inventory.inventorySlots[i].setQuantity(quantityToAdd);
                Main.inventory.inventoryElements[i].setToolTipText(itemToAdd.getName() +  " : " + Main.inventory.inventorySlots[i].slotNumber + " : " + Main.inventory.inventorySlots[i].quantity);
                Main.inventory.inventoryElements[i].setButtonIcon( 40, 40, "Textures/" + itemToAdd.getName() + "icon.png");
                break;
            } 
            
        }
    }
    
    public static void InitInventoryHelper(){
        
    }
    public static void RemoveFromInventory(String itemToRemove, int quantityToRemove){
        
        
    }
}
