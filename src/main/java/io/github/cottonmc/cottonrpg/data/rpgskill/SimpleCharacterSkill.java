package io.github.cottonmc.cottonrpg.data.rpgskill;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class SimpleCharacterSkill implements CharacterSkill {

	private int cooldown;
	private Prerequisite prereq;
	private BiFunction<PlayerEntity, Target<?>, Boolean> action;
	private List<Text> additionalLines = new ArrayList<>();

	public SimpleCharacterSkill(int cooldown, Prerequisite prereq, BiFunction<PlayerEntity, Target<?>, Boolean> action) {
		this.cooldown = cooldown;
		this.prereq = prereq;
		this.action = action;
	}

	@Override
	public int getCooldownTime() {
		return cooldown;
	}

	@Override
	public Prerequisite getRequirement() {
		return prereq;
	}

	@Override
	public boolean perform(PlayerEntity player, CharacterSkillEntry entry, Target<?> target) {
		if (canPerform(player, target)) {
			entry.startCooldown();
			return action.apply(player, target);
		}
		return false;
	}

	@Override
	public List<Text> getDescription() {
		List<Text> lines = new ArrayList<>();
		Identifier id = CottonRPG.SKILLS.getId(this);
		if (id != null) {
			for (int i = 0; i < 10; i++) {
				String key = "desc.skill." + id.getNamespace() + "." + id.getPath() + "." + i;
				if (!I18n.hasTranslation(key)) break;
				lines.add(new TranslatableText(key));
			}
		}
		lines.addAll(additionalLines);
		return lines;
	}

	@Override
	public void addAdditionalDescription(Text... lines) {
		additionalLines.addAll(Arrays.asList(lines));
	}
}
