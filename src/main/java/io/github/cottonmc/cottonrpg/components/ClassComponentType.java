package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;

public abstract class ClassComponentType {

  public abstract SimpleClassComponent construct(PlayerEntity player);
  
}
