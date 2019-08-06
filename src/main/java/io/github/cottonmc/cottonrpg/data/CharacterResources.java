package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CharacterResources {
  private PlayerEntity player;

  public CharacterResources(PlayerEntity player) {
    this.player = player;
  }

  private Map<Identifier, CharacterResourceEntry> underlying = new HashMap<>();
  //TODO: figure out how to listen to changes in individual resources too

  public boolean has(Identifier id) {
    return underlying.containsKey(id);
  }

  public CharacterResourceEntry get(Identifier id) {
    return underlying.get(id);
  }

  public void giveIfAbsent(CharacterResourceEntry clazz) {
    underlying.putIfAbsent(clazz.id, clazz);
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
