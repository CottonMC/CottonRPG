package io.github.cottonmc.cottonrpg.data;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;

public class CharacterResources {
  private ArrayList<Identifier> removed = new ArrayList<>();

  public CharacterResources() {
  }

  private Map<Identifier, CharacterResourceEntry> underlying = new HashMap<>();

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

  public CharacterResourceEntry get(Identifier id) {
    synchronized(underlying) {
      return underlying.get(id);
    }
  }

  public void giveIfAbsent(CharacterResourceEntry resource) {
    synchronized(underlying) {
      underlying.putIfAbsent(resource.id, resource);
    }
    resource.markDirty();
  }

  public CharacterResourceEntry remove(Identifier id) {
    CharacterResourceEntry entry = underlying.remove(id);
    if (entry!=null) removed.add(id);
    return entry;
  }

  public void forEach(BiConsumer<Identifier, CharacterResourceEntry> consumer) {
    synchronized(underlying) {
      underlying.forEach(consumer);
    }
  }

  public boolean isDirty() {
    if (!removed.isEmpty()) return true;

    for(CharacterResourceEntry entry : underlying.values()) {
      if (entry.isDirty()) return true;
    }

    return false;
  }

  public void sync(ServerPlayerEntity player) {
    if (!isDirty()) return;

    if (!removed.isEmpty()) {
      CottonRPGNetworking.syncAllResources(player, this);
      removed.clear();
    } else {
      //TODO: Do a partial sync for just those resources.
      CottonRPGNetworking.syncAllResources(player, this);
    }
  }
}
