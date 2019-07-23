package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.util.CharacterDataHolder;
import net.minecraft.entity.player.PlayerEntity;

public class CharacterData {
  private CharacterDataHolder holder;
  
  public static CharacterData get(PlayerEntity player) {
    CharacterData data = new CharacterData();
    data.holder = ((CharacterDataHolder)player);
    return data;
  }

  public CharacterClasses getClasses() {
    return holder.crpg_getClasses();
  }

  public CharacterResources getResources() {
    return holder.crpg_getResources();
  }

  //TODO: skills
  
}
