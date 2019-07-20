package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public abstract class SimpleResourceBarComponent implements ResourceBarComponent {

  public static final String KEY_MAX = "max";
  public static final String KEY_VALUE = "value";
  
  protected long max = getDefaultMax();
  protected long value = getDefaultValue();
  
  protected PlayerEntity owner;
  
  public SimpleResourceBarComponent(PlayerEntity owner) {
    this.owner = owner;
  }

  @Override
  public void fromTag(CompoundTag tag) {
    if (tag.containsKey(KEY_MAX))
      max = tag.getLong(KEY_MAX);
    if (tag.containsKey(KEY_VALUE))
      value = tag.getLong(KEY_VALUE);
  }

  @Override
  public CompoundTag toTag(CompoundTag tag) {
    tag.putLong(KEY_MAX, max);
    tag.putLong(KEY_VALUE, value);
    return tag;
  }

  @Override
  public PlayerEntity getPlayer() {
    return owner;
  }
  
  @Override
  public long getMax() {
    return max;
  }

  @Override
  public void setMax(long max) {
    this.max = max;
  }

  @Override
  public long getValue() {
    return value;
  }

  @Override
  public void setValue(long value) {
    if (value > max) {
      value = max;
    }
    this.value = value;
  }
  
  protected abstract long getDefaultMax();
  protected abstract long getDefaultValue();

}
