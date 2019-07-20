package io.github.cottonmc.cottonrpg.util;

import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import net.minecraft.util.Identifier;

public interface RPGPlayer {
  ClassComponent getRPGClass(Identifier id);
  ResourceBarComponent getRPGResourceBar(Identifier id);
}
