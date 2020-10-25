package io.github.cottonmc.cottonrpg.data.resource;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SimpleCharacterResource implements CharacterResource {
	private final int def;
	private final int max;
	private final int unitsPerBar;
	private final int ticksPerUnit;
	private final int color;
	private final ResourceVisibility vis;

	public SimpleCharacterResource(int defaultValue, int maxValue, int unitsPerBar, int ticksPerUnit, int color, ResourceVisibility vis) {
		this.def = defaultValue;
		this.max = maxValue;
		this.unitsPerBar = unitsPerBar;
		this.ticksPerUnit = ticksPerUnit;
		this.color = color;
		this.vis = vis;
	}

	@Override
	public int getUnitsPerBar() {
		return unitsPerBar;
	}

	@Override
	public int getDefaultMaxLevel() {
		return max;
	}

	@Override
	public int getDefaultLevel() {
		return def;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public ResourceVisibility getVisibility() {
		return vis;
	}

	@Override
	public Ticker makeTicker(CharacterResourceEntry entry) {
		//regen by one every <ticksPerUnit> ticks.
		return new SimpleTicker(ticksPerUnit, e -> e.setCurrent(e.getCurrent() + 1), e -> e.getCurrent() < e.getMax());
	}

	@Override
	public List<Text> getDescription() {
		List<Text> lines = new ArrayList<>();
		Identifier id = CottonRPG.RESOURCES.getId(this);
		if (id != null) {
			for (int i = 0; i < 10; i++) {
				String key = "desc.resource." + id.getNamespace() + "." + id.getPath() + "." + i;
				if (!I18n.hasTranslation(key)) break;
				lines.add(new TranslatableText(key));
			}
		}
		return lines;
	}
}
