package io.github.cottonmc.cottonrpg.util.skill;

import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.CharacterSkill;
import io.github.cottonmc.cottonrpg.data.CharacterSkillEntry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface SkillHandler<T> {

	/**
	 * Used internally. Do not call to enable a skill for a player.
	 * @param skill The skill to add to the list of skills to perform.
	 */
	void addSkill(CharacterSkill skill);

	/**
	 * @return The skills this handler manages.
	 */
	List<CharacterSkill> getSkills();

	/**
	 * Perform a single skill this handler manages.
	 * @param player The player performing the skill.
	 * @param target The target of the skill.
	 * @param skill The skill to perform.
	 */
	default void perform(PlayerEntity player, Target<T> target, CharacterSkill skill) {
		if (skill.canPerform(player, target)) {
			CharacterSkillEntry entry = CharacterData.get(player).getSkills().get(skill);
			skill.perform(player, entry, target);
			entry.startCooldown();
		}
	}

	/**
	 * Perform all skills this handler manages.
	 * @param player The player performing the skills.
	 * @param target The target of the skills.
	 */
	default void performAll(PlayerEntity player, Target<T> target) {
		for (CharacterSkill skill : getSkills()) {
			if (skill.canPerform(player, target)) {
				CharacterSkillEntry entry = CharacterData.get(player).getSkills().get(skill);
				skill.perform(player, entry, target);
				entry.startCooldown();
			}
		}
	}
}
