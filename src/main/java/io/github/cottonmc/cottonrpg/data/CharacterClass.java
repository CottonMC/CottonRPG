package io.github.cottonmc.cottonrpg.data;

import java.util.List;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public interface CharacterClass {

  /**
   * @return The class identifier. Yes, we need it for both entries and components.
   */
  Identifier getID();
  
  /**
   * @return The max level you can obtain with this class.
   */
  int getMaxLevel();

  /**
   * @param currentLevel The curent class level of the player seeking to level up.
   * @param player The player seeking to level up.
   * @return Whether the player can level up.
   */
  boolean canLevelUp(int currentLevel, PlayerEntity player);

  /**
   * Apply whatever cost is needed to level up a player.
   * @param previousLevel The class level of the player before they leveled up.
   * @param player The player leveling up.
   */
  void applyLevelUp(int previousLevel, PlayerEntity player);

  default String getTranslationKey() {
    Identifier id = CottonRPG.CLASSES.getId(this);
    return "class." + id.getNamespace() + "." + id.getPath();
  }

  default Text getName() {
    return new TranslatableText(getTranslationKey());
  }

  /**
   * @return The lines of description to display in the character sheet. Will be auto-wrapped.
   */
  @Environment(EnvType.CLIENT)
  List<Text> getClassDescription();

  /**
   * Allow other mods to add description if they use your player class.
   * @param lines The lines to add.
   */
  void addAdditionalDescription(Text... lines);

}
