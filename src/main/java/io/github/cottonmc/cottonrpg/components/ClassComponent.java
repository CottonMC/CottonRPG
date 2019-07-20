package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * Just a regular RPG class.
 * 
 * @see SimpleClassComponent
 */
public interface ClassComponent {
  PlayerEntity getPlayer();
  public void fromTag(CompoundTag tag);
  public CompoundTag toTag(CompoundTag tag);
  
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
