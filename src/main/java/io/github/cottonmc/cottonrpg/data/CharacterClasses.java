package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CharacterClasses {
  private ArrayList<Identifier> removed = new ArrayList<>();

  private final Map<Identifier, CharacterClassEntry> underlying = new HashMap<>();

  public int getSize() {
    synchronized(underlying) {
      return underlying.size();
    }
  }

  public void clear() {
    synchronized(underlying) {
      underlying.clear();
    }
  }

  public boolean has(Identifier id) {
    synchronized(underlying) {
      return underlying.containsKey(id);
    }
  }

  public CharacterClassEntry get(Identifier id) {
    synchronized(underlying) {
      return underlying.get(id);
    }
  }

  public void giveIfAbsent(CharacterClassEntry clazz) {
    synchronized(underlying) {
      underlying.putIfAbsent(clazz.id, clazz);
    }
    clazz.markDirty();
  }

  public CharacterClassEntry remove(Identifier id) {
    CharacterClassEntry entry = underlying.remove(id);
    if (entry!=null) removed.add(id);
    return entry;
  }

  public void forEach(BiConsumer<Identifier, CharacterClassEntry> consumer) {
    synchronized(underlying) {
      underlying.forEach(consumer);
    }
  }

  public boolean isDirty() {
    if (!removed.isEmpty()) return true;

    for(CharacterClassEntry entry : underlying.values()) {
      if (entry.isDirty()) return true;
    }

    return false;
  }

  public void sync(ServerPlayerEntity player) {
    if (!isDirty()) return;

    if (!removed.isEmpty()) {
      CottonRPGNetworking.batchSyncClasses(player, this, true);
      removed.clear();
    } else {
      CottonRPGNetworking.batchSyncClasses(player, this, false);
    }
  }

}
