package io.github.cottonmc.cottonrpg.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

/**
 * Flyweight for interacting with classes.
 * A CharacterClassEntry stores the Identifier of the class, the player's level of the class, and how much XP in the class the player has.
 * Class XP is separate from a player's XP, and the XP in other classes.
 * However, you may want to implement experience on your classes, so feel free to access this then.
 */
public class CharacterClassEntry {
  public Identifier id;
  private int level = 0;
  private int experience = 0;

  public CharacterClassEntry(Identifier id) {
    this.id = id;
  }

  public CompoundTag toTag() {
    CompoundTag tag = new CompoundTag();
    tag.putInt("level", this.level);
    tag.putInt("experience", this.experience);
    return tag;
  }

  public CharacterClassEntry fromTag(CompoundTag tag) {
    this.level = tag.getInt("level");
    this.experience = tag.getInt("experience");
    return this;
  }

  public int getLevel() {
    return this.level;
  }

  public void setLevel(int i) {
    this.level = i;
  }

  public int getExperience() {
    return this.experience;
  }

  public void setExperience(int i) {
    this.experience = i;
  }
}
