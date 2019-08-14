package io.github.cottonmc.cottonrpg.util.skill;

import io.github.cottonmc.cottonrpg.data.CharacterSkill;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ManualSkillHandler implements SkillHandler<PlayerEntity> {
	private List<CharacterSkill> skills = new ArrayList<>();

	@Override
	public void addSkill(CharacterSkill skill) {
		skills.add(skill);
	}

	@Override
	public List<CharacterSkill> getSkills() {
		return skills;
	}
}
