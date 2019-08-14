package io.github.cottonmc.cottonrpg.util.skill;

import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.CharacterSkill;
import io.github.cottonmc.cottonrpg.data.CharacterSkillEntry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface SkillHandler<T> {

	void addSkill(CharacterSkill skill);

	List<CharacterSkill> getSkills();

	default void perform(PlayerEntity player) {
		for (CharacterSkill skill : getSkills()) {
			Target target = skill.createTarget(player);
			if (skill.canPerform(player, target)) {
				CharacterSkillEntry entry = CharacterData.get(player).getSkills().get(skill);
				skill.perform(player, entry, target);
				entry.startCooldown();
			}
		}
	}
}
