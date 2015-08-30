/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;

/**
 *
 * @author christian
 */
public final class Statics {
   static Material s_Material = new Material(Main.s_AssetManager, "Common/MatDefs/Misc/Unshaded.j3md");
   static boolean s_PlayerSettingModel = false;
   static float tpf;
   static boolean s_ShowInventory = false;
}
