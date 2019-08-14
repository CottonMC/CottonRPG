package io.github.cottonmc.cottonrpg.util.skill;

import io.github.cottonmc.cottonrpg.data.CharacterSkill;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A handler for skills that are manually, explicitly activated.
 */
public class ActiveSkillHandler implements SkillHandler {
	private List<CharacterSkill> skills = new ArrayList<>();

	@Override
	public void addSkill(CharacterSkill skill) {
		skills.add(skill);
	}

	@Override
	public List<CharacterSkill> getSkills() {
		return skills;
	}

	@Override
	public void performAll(PlayerEntity player, Target target) {
		throw new UnsupportedOperationException("You cannot perform all active skills with a single Target object!");
	}
}
