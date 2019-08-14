package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleCharacterSkill implements CharacterSkill<ManualSkill> {
	private int cooldown;
	private Prerequisite prereq;
	private ManualSkill action;
	private List<Text> additionalLines;

	public SimpleCharacterSkill(int cooldown, Prerequisite prereq, ManualSkill action) {
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
	public ManualSkill getCallback() {
		return action;
	}

	@Nullable
	@Override
	public Event getEvent() {
		//no event, this skill is run manually
		return null;
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
