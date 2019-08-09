package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleCharacterResource implements CharacterResource {
  private long def;
  private long max;
  private long unitsPerBar;
  private int color;
  private ResourceVisibility vis;

  public SimpleCharacterResource(long defaultValue, long maxValue, long unitsPerBar, int color, ResourceVisibility vis) {
    this.def = defaultValue;
    this.max = maxValue;
    this.unitsPerBar = unitsPerBar;
    this.color = color;
    this.vis = vis;
  }

  @Override
  public long getUnitsPerBar() {
    return unitsPerBar;
  }

  @Override
  public long getDefaultMaxLevel() {
    return max;
  }

  @Override
  public long getDefaultLevel() {
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

  @Override
  public void tick(CharacterResourceEntry entry) {
    //do nothing
  }
}
