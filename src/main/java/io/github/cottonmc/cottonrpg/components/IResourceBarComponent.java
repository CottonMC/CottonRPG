package io.github.cottonmc.cottonrpg.components;

import java.awt.Color;

import net.minecraft.util.Identifier;

/**
 * Describes a resource like a health bar or a mana bar
 */
public interface IResourceBarComponent {

  public enum ResourceVisibility {
    INVISIBLE,
    MENU,
    HUD
  }
  
  long getMax();
  void setMax(long max);
  
  long getValue();
  void setValue(long value);
  
  void tick();
  
  Identifier getID();
  
  Color getColor();
  ResourceVisibility getVisibility();
}
