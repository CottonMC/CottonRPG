package io.github.cottonmc.cottonrpg.components;

import io.github.cottonmc.cottonrpg.util.Ticker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public abstract class SimpleResourceBarComponent implements ResourceBarComponent {

  public static final String KEY_MAX = "max";
  public static final String KEY_VALUE = "value";
  
  protected Ticker ticker;
  
  protected long max;
  protected long value;
  
  protected ResourceBarComponentType type;
  protected PlayerEntity owner;
  
  public SimpleResourceBarComponent(ResourceBarComponentType type, PlayerEntity player) {
    this.ticker = type.makeTicker(this);
    this.type = type;
    this.max = type.getDefaultMax();
    this.value = type.getDefaultValue();
    this.owner = player;
  }

  @Override
  public ResourceBarComponentType getType() {
    return type;
  }
  
  @Override
  public PlayerEntity getPlayer() {
    return owner;
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
  synchronized public void setValue(long value) {
    if (value > max) {
      value = max;
    }
    this.value = value;
  }
  
  @Override
  synchronized public void updateValue(long upd) {
    long value = this.value + upd;
    if (value > max) {
      value = max;
    }
    this.value = value;
  }
  
  @Override
  public void tick() {
    ticker.tick();
  }

}
