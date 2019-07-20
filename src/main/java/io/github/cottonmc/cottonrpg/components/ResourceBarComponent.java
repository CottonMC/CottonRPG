package io.github.cottonmc.cottonrpg.components;

import java.awt.Color;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * Describes a resource like a health bar or a mana bar
 */
public interface ResourceBarComponent {

  public enum ResourceVisibility {
    INVISIBLE,
    MENU,
    HUD
  }
  
  PlayerEntity getPlayer();
  public void fromTag(CompoundTag tag);
  public CompoundTag toTag(CompoundTag tag);
  
  long getMax();
  void setMax(long max);
  
  long getValue();
  void setValue(long value);
  
  void tick();
  
  Identifier getID();
  
  Color getColor();
  ResourceVisibility getVisibility();
}
