package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * Describes a resource like a health bar or a mana bar
 */
public interface ResourceBarComponent {
  
  ResourceBarComponentType getType();
  PlayerEntity getPlayer();
  public void fromTag(CompoundTag tag);
  public CompoundTag toTag(CompoundTag tag);
  
  long getMax();
  void setMax(long max);
  
  long getValue();
  void setValue(long value);
  void updateValue(long upd);
    
  Identifier getID();
  
  void tick();
}
