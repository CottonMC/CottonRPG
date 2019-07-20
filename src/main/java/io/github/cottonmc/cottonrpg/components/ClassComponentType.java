package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;

@FunctionalInterface
public interface ClassComponentType {

  /*
   * Constructs a new class component instance.
   */
  IClassComponent construct(PlayerEntity player);
  
}
