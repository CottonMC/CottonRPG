package io.github.cottonmc.cottonrpg.components;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerComponentType<C> {
  C construct(PlayerEntity p);
}
