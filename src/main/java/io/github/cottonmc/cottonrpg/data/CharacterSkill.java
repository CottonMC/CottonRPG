package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
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

	default boolean canPerform(PlayerEntity player) {
		Identifier id = CottonRPG.SKILLS.getId(this);
		CharacterSkills skills = CharacterData.get(player).getSkills();
		if (!skills.has(id)) return false;
		CharacterSkillEntry entry = skills.get(id);
		return entry.getCooldown() <= 0;
	}

	default void tick(CharacterSkillEntry entry) {
		if (entry.getCooldown() > 0) entry.setCooldown(entry.getCooldown() - 1);
	}

	/**
	 * @return A lambda of the interface to use when running. Needs a PlayerEntity instance for checking if it's currently possible.
	 */
	//TODO: this always requires a `canPerform()` at the start of the lambda, is that avoidable?
	//TODO: this always requires a manual call to `CharacterData.get(player).getSkills().get(CottonRPG.SKILLS.getId(this)).startCooldown()` at the end, is that avoidable?
	T getCallback();

	/**
	 * @return The Event to register to, or null if there is no associated event and you want to call this manually.
	 */
	@Nullable
	Event getEvent();

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
