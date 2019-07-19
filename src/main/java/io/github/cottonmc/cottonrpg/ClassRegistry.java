package io.github.cottonmc.cottonrpg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.github.cottonmc.cottonrpg.components.IClassComponent;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ClassRegistry {
  private static ConcurrentHashMap<Identifier, ComponentType<IClassComponent>> registry
    = new ConcurrentHashMap<>();
  
  public static void register(Identifier id, ComponentType<IClassComponent> comp, Function<PlayerEntity, IClassComponent> s) {
    registry.put(id, comp);
    
    EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> {
      components.put(comp, s.apply(player));
    });
  }
  
  public static ComponentType<IClassComponent> get(Identifier id) {
    return registry.get(id);
  }
  
  public static void forEach(BiConsumer<Identifier, ComponentType<IClassComponent>> cons) {
    registry.forEach(cons);
  }
}
