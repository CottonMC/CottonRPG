package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;

@FunctionalInterface
public interface ResourceBarComponentType {

  ResourceBarComponent construct(PlayerEntity player);

}
