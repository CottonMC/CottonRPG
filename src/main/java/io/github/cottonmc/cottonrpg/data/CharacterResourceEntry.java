package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class CharacterResourceEntry {
  public final Identifier id;
  private CharacterResource res;
  private long current;
  private long max;

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
    return this;
  }

  public long getCurrent() {
    return current;
  }
  
  public long getMax() {
    return max;
  }
  
  public void tick() {
    res.tick(this);
  }
}