package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public interface CharacterSkill<T> {
	/**
	 * @return How many ticks this skill takes to cool down after using.
	 */
	int getCooldownTime();

	/**
	 * @return The set of requirements to unlock a skill.
	 */
	Prerequisite getRequirement();

	/**
	 * Perform this skill's action.
	 * @return A lambda of the event callback to use.
	 */
	T perform();

	default String getTranslationKey() {
		Identifier id = CottonRPG.SKILLS.getId(this);
		return "skill." + id.getNamespace() + "." + id.getPath();
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
	 * @param lines The lines to add.
	 */
	void addAdditionalDescription(Text... lines);
}
