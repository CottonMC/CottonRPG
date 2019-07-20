package io.github.cottonmc.cottonrpg;

import io.github.cottonmc.cottonrpg.components.ResourceBarComponentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class ResourceBarRegistry extends SimpleRegistry<ResourceBarComponentType> {
  
  public static final ResourceBarRegistry INSTANCE = new ResourceBarRegistry();
  
  public static void register(Identifier id, ResourceBarComponentType rbct) {
    Registry.register(INSTANCE, id, rbct);
  }
}
