/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;

/**
 *
 * @author christian
 */
public class Rect {
    
    public float x;
    public float y;
    public float width;
    public float height;
    
    public Rect(float x, float y, float width, float height){
        this.x = x; 
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void setX(float x){
        this.x = x;
    }
    
    public void setY(float y){
        this.y = y;
    }
    public boolean Contains(Vector2f pos){
        if(pos.x > x && pos.x < width && pos.y < height && pos.y > y){
            return true;
        }
        else
            return false;
    }
}
