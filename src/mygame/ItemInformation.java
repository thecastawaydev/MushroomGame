/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import java.util.HashMap;

/**
 *
 * @author christian
 */
public class ItemInformation {
    
    public static HashMap<String, Item> table;
    
    public static void InitItemInfo()
    {
        table = new HashMap<String, Item>();
        setUpTree();
        setUpRock();
    }
    
    private static void setUpTree()
    {
        table.put("tree", new Item(0.8f, new CapsuleCollisionShape(0.5f, 1)));
    }
    
    private static void setUpRock()
    {
        table.put("rock", new Item(1f, new CapsuleCollisionShape(0.5f, 1)));
    }
}
