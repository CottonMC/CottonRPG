package io.github.cottonmc.cottonrpg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.ClassComponentType;
import io.github.cottonmc.cottonrpg.components.PlayerComponentType;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

public class CharacterDataHolder {
  
  public static class DataAccessor<T, C extends PlayerComponentType<T>> {
    private final PlayerEntity player;
    private final SimpleRegistry<C> registry;
     
    private final ConcurrentHashMap<Identifier, T> data;
    
    public DataAccessor(PlayerEntity p, SimpleRegistry<C> r) {
      player = p;
      registry = r;
      data = new ConcurrentHashMap<>();
    }
    
    public void forEach(BiConsumer<Identifier, T> cons) {
      data.forEach(cons);
    }
    
    public T get(Identifier id) {
      return data.get(id);
    }
    
    public T enable(Identifier id) {
      if (data.containsKey(id)) {
        return data.get(id);
      }
      T d = registry.get(id).construct(player);
      data.put(id, d);
      return d;
    }
    
    public void disable(Identifier id) {
      data.remove(id);
    }
    
    public ConcurrentHashMap<Identifier, T> accessMap() {
      return data;
    }
  }
  
  public final DataAccessor<ClassComponent, ClassComponentType> classes;
  public final DataAccessor<ResourceBarComponent, ResourceBarComponentType> resourceBars;

  public CharacterDataHolder(PlayerEntity p) {
    classes = new DataAccessor<>(p, ClassRegistry.INSTANCE);
    resourceBars = new DataAccessor<>(p, ResourceBarRegistry.INSTANCE);
  }
  
}
