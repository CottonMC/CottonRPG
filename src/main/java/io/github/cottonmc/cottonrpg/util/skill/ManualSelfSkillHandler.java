package io.github.cottonmc.cottonrpg.util.skill;

import io.github.cottonmc.cottonrpg.data.CharacterSkill;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ManualSelfSkillHandler implements SkillHandler<PlayerEntity> {
	private List<CharacterSkill> skills = new ArrayList<>();

	public void perform(PlayerEntity player) {
		perform(player, createTarget(Collections.singleton(player)));
	}

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
