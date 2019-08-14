package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import io.github.cottonmc.cottonrpg.util.skill.SkillHandler;
import io.github.cottonmc.cottonrpg.util.skill.Target;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleCharacterSkill implements CharacterSkill {
	private int cooldown;
	private Prerequisite prereq;
	private List<Text> additionalLines;
	private TriConsumer<PlayerEntity, CharacterSkillEntry, Target<?>> action;

	public SimpleCharacterSkill(int cooldown, Prerequisite prereq, TriConsumer<PlayerEntity, CharacterSkillEntry, Target<?>> action) {
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
	public void perform(PlayerEntity player, CharacterSkillEntry entry, Target<?> target) {
		action.accept(player, entry, target);
	}

	@Nullable
	@Override
	public SkillHandler getHandler() {
		return CottonRPG.SELF_HANDLER;
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
