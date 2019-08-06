package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CharacterResourceEntry {
  public final Identifier id;
  private PlayerEntity player;
  private CharacterResource res;
  private long current;
  private long max;

  public CharacterResourceEntry(Identifier id, PlayerEntity player) {
    this.id = id;
    this.player = player;
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

  public void setCurrent(long l) {
    this.current = l;
    if (player instanceof ServerPlayerEntity) CottonRPGNetworking.syncResourceChange((ServerPlayerEntity)player, this);
  }
  
  public long getMax() {
    return max;
  }

  public void setMax(long l) {
    this.max = l;
    if (player instanceof ServerPlayerEntity) CottonRPGNetworking.syncResourceChange((ServerPlayerEntity)player, this);
  }
  
  public void tick() {
    res.tick(this);
  }
}