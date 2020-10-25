package io.github.cottonmc.cottonrpg.data.rpgskill;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.RpgDataType;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

public interface CharacterSkill extends RpgDataType {
	@Override
	default Identifier getId() {
		return Objects.requireNonNull(CottonRPG.SKILLS.getId(this), this + " is not a registered skill");
	}

	/**
	 * @return How many ticks this skill takes to cool down after using.
	 */
	int getCooldownTime();

	/**
	 * @return The set of requirements to unlock a skill.
	 */
	Prerequisite getRequirement();

	/**
	 * @param player The player attempting to perform a skill.
	 * @param target The target of the skill to perform.
	 * @return Whether the player can currently perform the skill.
	 */
	default boolean canPerform(PlayerEntity player, Target<?> target) {
		CharacterSkills skills = CharacterData.get(player).getSkills();
		if (!skills.has(this)) return false;
		CharacterSkillEntry entry = skills.get(this);
		return entry.getCooldown() <= 0;
	}

	/**
	 * Ticks the cooldown of this skill.
	 *
	 * @param entry The entry form of the skill.
	 */
	default void tick(CharacterSkillEntry entry) {
		if (entry.getCooldown() > 0) entry.setCooldown(entry.getCooldown() - 1);
	}

	/**
	 * Perform a skill.
	 *
	 * @param player The player performing the skill.
	 * @param target The target being performed on.
	 * @return Whether the skill succeeded or not.
	 */
	boolean perform(PlayerEntity player, CharacterSkillEntry entry, Target<?> target);

	default String getTranslationKey() {
		Identifier id = this.getId();
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
	 *
	 * @param lines The lines to add.
	 */
	void addAdditionalDescription(Text... lines);
}
