package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public abstract class ClassComponent implements IClassComponent {
  public static String KEY_LEVEL = "level";

  protected int level = 0;
  
  protected PlayerEntity owner;
  
  public ClassComponent(PlayerEntity owner) {
    this.owner = owner;
  }
  
  @Override
  public Entity getEntity() {
    return owner;
  }
  
  @Override
  public int getLevel() {
    return level;
  }
  
  @Override
  public CompoundTag toTag(CompoundTag tag) {
    tag.putInt(KEY_LEVEL, level);
    return tag;
  }
  
  @Override
  public void fromTag(CompoundTag tag) {
    if (tag.containsKey(KEY_LEVEL)) {
      level = tag.getInt(KEY_LEVEL);
    }
  }
}
