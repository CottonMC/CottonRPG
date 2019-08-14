package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.util.resource.Ticker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class CharacterResourceEntry {
  public final Identifier id;
  private CharacterResource res;
  private long current;
  private long max;
  private Ticker ticker;
  private transient boolean dirty = false;

  public CharacterResourceEntry(Identifier id) {
    this.id = id;
    this.res = CottonRPG.RESOURCES.get(id);
    current = res.getDefaultLevel();
    max = res.getDefaultMaxLevel();
    this.ticker = res.makeTicker(this);
  }

  public CompoundTag toTag() {
    CompoundTag tag = new CompoundTag();
    tag.putLong("CurrentLevel", current);
    tag.putLong("MaxLevel", max);
    tag.put("Ticker", ticker.toTag());
    return tag;
  }

  public CharacterResourceEntry fromTag(CompoundTag tag) {
    this.current = tag.getLong("CurrentLevel");
    this.max = tag.getLong("MaxLevel");
    this.ticker = res.makeTicker(this).fromTag(tag.getCompound("Ticker"));
     markDirty();
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
    ticker.tick(this);
    if (ticker.isDirty()) {
      markDirty();
      ticker.clearDirty();
    }
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