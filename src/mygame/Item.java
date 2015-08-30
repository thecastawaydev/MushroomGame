/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;

/**
 *
 * @author christian
 */
public class Item {
    public float scale;
    public CollisionShape collision;
    public int health;
    
    public Item(float scale, CollisionShape collision){
        this.scale = scale;
        this.collision = collision;
        this.health = 100;
    }
    
    public float getScale(){
        return scale;
    }
    
    public CollisionShape getCollision(){
        return collision;
    }
    
    public int getHealth(){
        return health;
    }
    
    public void setScale(float scale){
        this.scale = scale;
    }
    
    public void setCollision(CollisionShape collision){
        this.collision = collision;
    }

}
