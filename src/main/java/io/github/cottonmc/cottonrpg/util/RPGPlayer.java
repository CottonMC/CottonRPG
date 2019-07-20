package io.github.cottonmc.cottonrpg.util;

import java.util.function.BiConsumer;

import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import net.minecraft.util.Identifier;

public interface RPGPlayer {
  ClassComponent getRPGClass(Identifier id);
  void forEachRPGClass(BiConsumer<Identifier, ClassComponent> cons);
  ResourceBarComponent getRPGResourceBar(Identifier id);
  void forEachRPGResourceBar(BiConsumer<Identifier, ResourceBarComponent> cons);
}
