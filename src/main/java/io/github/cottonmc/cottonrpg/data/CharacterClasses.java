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

public class CharacterClasses {
  private PlayerEntity player;

  public CharacterClasses(PlayerEntity player) {
    this.player = player;
  }

  private Map<Identifier, CharacterClassEntry> underlying = new HashMap<>();

  public boolean has(Identifier id) {
    return underlying.containsKey(id);
  }

  public CharacterClassEntry get(Identifier id) {
    return underlying.get(id);
  }

  public void giveIfAbsent(CharacterClassEntry clazz) {
    underlying.putIfAbsent(clazz.id, clazz);
    markDirty();
  }

  public CharacterClassEntry remove(Identifier id) {
    CharacterClassEntry entry = underlying.remove(id);
    markDirty();
    return entry;
  }

  public void forEach(BiConsumer<Identifier, CharacterClassEntry> consumer) {
    underlying.forEach(consumer);
  }

  public void markDirty() {
    if (player instanceof ServerPlayerEntity) CottonRPGNetworking.syncAllClasses((ServerPlayerEntity)player, this);
  }

}
