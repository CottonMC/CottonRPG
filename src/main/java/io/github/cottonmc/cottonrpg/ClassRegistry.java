package io.github.cottonmc.cottonrpg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.IClassComponent;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.util.Identifier;

public class ClassRegistry {
  private static ConcurrentHashMap<Identifier, ComponentType<IClassComponent>> registry
    = new ConcurrentHashMap<>();
  
  public static void register(Identifier id, ComponentType<IClassComponent> comp) {
    registry.put(id, comp);
  }
  
  public static void forEach(BiConsumer<Identifier, ComponentType<IClassComponent>> cons) {
    registry.forEach(cons);
  }
}
