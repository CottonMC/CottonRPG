package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class CharacterResourceEntry {
  public final Identifier id;
  private CharacterResource res;
  private long current;
  private long max;
  private transient boolean dirty = false;

  public CharacterResourceEntry(Identifier id) {
    this.id = id;
    this.res = CottonRPG.RESOURCES.get(id);
    current = res.getDefaultLevel();
    max = res.getDefaultMaxLevel();
  }

  public CompoundTag toTag() {
    CompoundTag tag = new CompoundTag();
    tag.putLong("CurrentLevel", current);
    tag.putLong("MaxLevel", max);
    return tag;
  }

  public CharacterResourceEntry fromTag(CompoundTag tag) {
    this.current = tag.getLong("CurrentLevel");
    this.max = tag.getLong("MaxLevel");
    dirty = true;
    return this;
  }

  public long getCurrent() {
    return current;
  }

  public void setCurrent(long l) {
    markDirty();
    this.current = l;
  }
  
  public long getMax() {
    return max;
  }

  public void setMax(long l) {
    markDirty();
    this.max = l;
  }
  
  public void tick() {
    res.tick(this);
  }
  
  public void markDirty() {
    dirty = true;
  }
  
  public boolean isDirty() {
    return dirty;
  }

  public void clearDirty() {
    dirty = false;
  }
}