package io.github.cottonmc.cottonrpg;

import io.github.cottonmc.cottonrpg.components.ClassComponentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class ClassRegistry extends SimpleRegistry<ClassComponentType> {
  
  public static final ClassRegistry INSTANCE = new ClassRegistry();
  
  public static void register(Identifier id, ClassComponentType cct) {
    Registry.register(INSTANCE, id, cct);
  }
}
