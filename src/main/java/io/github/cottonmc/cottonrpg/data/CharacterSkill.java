package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import io.github.cottonmc.cottonrpg.util.skill.SkillHandler;
import io.github.cottonmc.cottonrpg.util.skill.Target;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.List;

public interface CharacterSkill {
	/**
	 * @return How many ticks this skill takes to cool down after using.
	 */
	int getCooldownTime();

	/**
	 * @return The set of requirements to unlock a skill.
	 */
	Prerequisite getRequirement();

	default boolean canPerform(PlayerEntity player, Target<?> target) {
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
	void perform(PlayerEntity player, CharacterSkillEntry entry, Target<?> target);

	/**
	 * @return The handler that should manage this skill, or null if this skill is only ever run manually.
	 */
	@Nullable
	SkillHandler getHandler();

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
