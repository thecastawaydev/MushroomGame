/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.awt.Rectangle;

/**
 *
 * @author christian
 */
public class InventorySlot {
    public int slotNumber;
    public int quantity = 0;
    public String itemName;
    public Rect rect;
    
    public InventorySlot(int slotNumber, int quantity, String itemName){
        this.slotNumber = slotNumber;
        this.quantity = quantity;
        this.itemName = itemName;
        this.rect = rect;
    }
    
    public void setName(String name){
        this.itemName = name;
    }
    
    public void setQuantity(int number){
        this.quantity = number;
    }
}
