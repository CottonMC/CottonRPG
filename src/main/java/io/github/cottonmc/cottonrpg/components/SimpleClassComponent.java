package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public abstract class SimpleClassComponent implements ClassComponent {
  public static String KEY_LEVEL = "level";

  protected int level = 0;
  
  protected ClassComponentType type;
  protected PlayerEntity owner;
  
  public SimpleClassComponent(ClassComponentType type, PlayerEntity owner) {
    this.owner = owner;
  }
  
  @Override
  public ClassComponentType getType() {
    return type;
  }
  
  @Override
  public PlayerEntity getPlayer() {
    return owner;
  }
  
  @Override
  public int getLevel() {
    return level;
  }
  
  @Override
  public void setLevel(int level) {
    this.level = level;
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
