package io.github.cottonmc.cottonrpg.data;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CharacterResources {
  private Map<Identifier, CharacterResourceEntry> underlying = new HashMap<>();
  //TODO: figure out how to listen to changes in individual resources too
  private List<Consumer<CharacterResources>> listeners = new ArrayList<>();

  public boolean has(Identifier id) {
    return underlying.containsKey(id);
  }

  public CharacterResourceEntry get(Identifier id) {
    return underlying.get(id);
  }

  public void giveIfAbsent(CharacterResourceEntry clazz) {
    underlying.putIfAbsent(clazz.id, clazz);
    listeners.forEach(listener -> listener.accept(this));
  }

  public CharacterResourceEntry remove(Identifier id) {
    CharacterResourceEntry entry = underlying.remove(id);
    listeners.forEach(listener -> listener.accept(this));
    return entry;
  }

  public void forEach(BiConsumer<Identifier, CharacterResourceEntry> consumer) {
    underlying.forEach(consumer);
  }

  public void listen(Consumer<CharacterResources> listener) {
    listeners.add(listener);
  }
}
