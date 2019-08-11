package io.github.cottonmc.cottonrpg.util;

import io.github.cottonmc.cottonrpg.data.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.CharacterResources;

public interface CharacterDataHolder {
  CharacterClasses crpg_getClasses();
  CharacterResources crpg_getResources();
  void crpg_setClasses(CharacterClasses classes);
  void crpg_setResources(CharacterResources resources);
}
