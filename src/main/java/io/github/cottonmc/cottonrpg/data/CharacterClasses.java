package io.github.cottonmc.cottonrpg.data;

import com.sun.istack.internal.Nullable;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CharacterClasses {
  private Map<Identifier, CharacterClassEntry> underlying = new HashMap<>();
  //TODO: figure out how to listen to changes in individual classes too
  private List<Consumer<CharacterClasses>> listeners = new ArrayList<>();

  public boolean has(Identifier id) {
    return underlying.containsKey(id);
  }

  public CharacterClassEntry get(Identifier id) {
    return underlying.get(id);
  }

  public void giveIfAbsent(CharacterClassEntry clazz) {
    underlying.putIfAbsent(clazz.id, clazz);
    listeners.forEach(listener -> listener.accept(this));
  }

  public @Nullable CharacterClassEntry remove(Identifier id) {
    CharacterClassEntry entry = underlying.remove(id);
    listeners.forEach(listener -> listener.accept(this));
    return entry;
  }

  public void forEach(BiConsumer<Identifier, CharacterClassEntry> consumer) {
    underlying.forEach(consumer);
  }

  public void listen(Consumer<CharacterClasses> listener) {
    listeners.add(listener);
  }
}
