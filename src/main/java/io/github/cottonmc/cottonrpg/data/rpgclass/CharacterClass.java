package io.github.cottonmc.cottonrpg.data.rpgclass;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.RpgDataType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

public interface CharacterClass extends RpgDataType {

	@Override
	default Identifier getId() {
		return Objects.requireNonNull(CottonRPG.CLASSES.getId(this), this + " is not a registered class");
	}

	/**
	 * @return The max level you can obtain with this class.
	 */
	int getMaxLevel();

	/**
	 * @param currentLevel The curent class level of the player seeking to level up.
	 * @param player       The player seeking to level up.
	 * @return Whether the player can level up.
	 */
	boolean canLevelUp(int currentLevel, PlayerEntity player);

	/**
	 * Apply whatever cost is needed to level up a player.
	 *
	 * @param previousLevel The class level of the player before they leveled up.
	 * @param player        The player leveling up.
	 */
	void applyLevelUp(int previousLevel, PlayerEntity player);

	default String getTranslationKey() {
		Identifier id = this.getId();
		return "class." + id.getNamespace() + "." + id.getPath();
	}

	default Text getName() {
		return new TranslatableText(getTranslationKey());
	}

	/**
	 * @return The lines of description to display in the character sheet. Will be auto-wrapped.
	 */
	@Environment(EnvType.CLIENT)
	List<Text> getDescription();

	/**
	 * Allow other mods to add description if they use your player class.
	 *
	 * @param lines The lines to add.
	 */
	void addAdditionalDescription(Text... lines);

}
