package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CharacterResources {
  private PlayerEntity player;

  public CharacterResources(PlayerEntity player) {
    this.player = player;
  }

  private Map<Identifier, CharacterResourceEntry> underlying = new HashMap<>();

  public int getSize() {
    return underlying.size();
  }

  public void clear() {
    underlying.clear();
  }

  public boolean has(Identifier id) {
    return underlying.containsKey(id);
  }

  public CharacterResourceEntry get(Identifier id) {
    return underlying.get(id);
  }

  public void giveIfAbsent(CharacterResourceEntry resource) {
    underlying.putIfAbsent(resource.id, resource);
    markDirty();
  }

  public CharacterResourceEntry remove(Identifier id) {
    CharacterResourceEntry entry = underlying.remove(id);
    markDirty();
    return entry;
  }

  public void forEach(BiConsumer<Identifier, CharacterResourceEntry> consumer) {
    underlying.forEach(consumer);
  }

  public void markDirty() {
    if (player instanceof ServerPlayerEntity) CottonRPGNetworking.syncAllResources((ServerPlayerEntity)player, this);
  }

}
