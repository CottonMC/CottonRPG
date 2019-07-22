package io.github.cottonmc.cottonrpg.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.github.cottonmc.cottonrpg.util.Ticker;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public abstract class ResourceBarComponentType implements PlayerComponentType<ResourceBarComponent> {

  public enum ResourceVisibility {
    INVISIBLE,
    MENU,
    HUD
  }
  
  public abstract ResourceBarComponent construct(PlayerEntity player);
  public abstract Identifier getID();
  
  public abstract Ticker makeTicker(ResourceBarComponent rb);

  public abstract Color getColor();
  public abstract ResourceVisibility getVisibility();
  
  protected long getDefaultMax() {
    return 0;
  }
  protected long getDefaultValue() {
    return 16;
  }
  

  protected static ConcurrentHashMap<Identifier, Text> names =
      new ConcurrentHashMap<>();
  protected static ConcurrentHashMap<Identifier, List<Text>> descriptions =
      new ConcurrentHashMap<>();
  
  public Text getName() {
    Identifier id = getID();
    if (names.contains(id))
      return names.get(id);
    String key = "name.rpgresource." + id.getNamespace() + "." + id.getPath();
    Text t = null;
    if (I18n.hasTranslation(key)) {
      t = new TranslatableText(key);
    } else {
      t = new LiteralText(key);
    }
    names.put(id, t);
    return t;
  }
  
  public List<Text> getDescription() {
    Identifier id = getID();
    if (descriptions.contains(id))
      return descriptions.get(id);
    List<Text> lines = new ArrayList<>();
    if (id != null) {
      int i = 0;
      while (true) {
        String key = "desc.rpgresource." + id.getNamespace() + "." + id.getPath() + "." + i;
        if (!I18n.hasTranslation(key)) break;
        lines.add(new TranslatableText(key));
        i++;
      }
    }
    descriptions.put(id, lines);
    return lines;
  }
  
}
