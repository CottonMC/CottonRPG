package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleCharacterClass implements CharacterClass {
  private int maxLevel;
  private List<Text> additionalLines = new ArrayList<>();

  public SimpleCharacterClass(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  @Override
  public int getMaxLevel() {
    return maxLevel;
  }

  private int getExperienceCost(int currentLevel) {
    if (currentLevel > 3) return 30;
    if (currentLevel == 0) return 5;
    return 10*currentLevel;
  }

  @Override
  public boolean canLevelUp(int currentLevel, PlayerEntity player) {
    //TODO: swap to predicate system for simple?
    int expCost = getExperienceCost(currentLevel);
    return player.experienceLevel >= expCost;
  }

  @Override
  public void applyLevelUp(int previousLevel, PlayerEntity player) {
    int expCost = getExperienceCost(previousLevel);
    player.experienceLevel -= expCost;
  }

  @Override
  public List<Text> getClassDescription() {
    List<Text> lines = new ArrayList<>();
    Identifier id = CottonRPG.CLASSES.getId(this);
    if (id != null) {
      for (int i = 0; i < 10; i++) {
        String key = "desc.class." + id.getNamespace() + "." + id.getPath() + "." + i;
        if (!I18n.hasTranslation(key)) break;
        lines.add(new TranslatableText(key));
      }
    }
    lines.addAll(additionalLines);
    return lines;
  }

  @Override
  public void addAdditionalDescription(Text... lines) {
    additionalLines.addAll(Arrays.asList(lines));
  }
}
