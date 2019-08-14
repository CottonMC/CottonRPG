package io.github.cottonmc.cottonrpg.util.skill;

import io.github.cottonmc.cottonrpg.data.CharacterSkill;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;
import java.util.List;

public class SelfSkillHandler implements SkillHandler<PlayerEntity> {
	private List<CharacterSkill> skills;

	@Override
	public void addSkill(CharacterSkill skill) {
		skills.add(skill);
	}

	@Override
	public List<CharacterSkill> getSkills() {
		return skills;
	}

	@Override
	public Target<PlayerEntity> createTarget(Collection<PlayerEntity> target) {
		return new PlayerTarget(target);
	}
}
