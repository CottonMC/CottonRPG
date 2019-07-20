package io.github.cottonmc.cottonrpg.components;

import net.minecraft.util.Identifier;

/**
 * Just a regular RPG class.
 * 
 * @see ClassComponent
 */
public interface IClassComponent {
  /**
   * Invisible if <0
   * Unlockable if =0
   * Unlocked and possibly affects skill power if >0
   * @return Class level
   */  
  int getLevel();
  
  void setLevel(int level);
  
  Identifier getID();
}
