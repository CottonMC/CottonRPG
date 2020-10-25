package io.github.cottonmc.cottonrpg.data.clazz;

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
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimpleCharacterClass implements CharacterClass {
	private int maxLevel;
	private Function<Integer, Prerequisite> prereq;
	private BiConsumer<Integer, PlayerEntity> levelConsumer;
	private List<Text> additionalLines = new ArrayList<>();

	public SimpleCharacterClass(int maxLevel, Function<Integer, Prerequisite> prereq, BiConsumer<Integer, PlayerEntity> levelConsumer) {
		this.maxLevel = maxLevel;
		this.prereq = prereq;
		this.levelConsumer = levelConsumer;
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public boolean canLevelUp(int currentLevel, PlayerEntity player) {
		return prereq.apply(currentLevel).test(player);
	}

	@Override
	public void applyLevelUp(int previousLevel, PlayerEntity player) {
		levelConsumer.accept(previousLevel, player);
	}

	@Override
	public List<Text> getDescription() {
		List<Text> lines = new ArrayList<>();
		Identifier id = CottonRPG.CLASSES.getId(this);
		if (id != null) {
			for (int i = 0; i < 10; i++) {
				String key = "desc.class." + id.getNamespace() + "." + id.getPath() + "." + i;
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
