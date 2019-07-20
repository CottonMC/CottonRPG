package io.github.cottonmc.cottonrpg.components;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public abstract class ClassComponentType {

  public abstract SimpleClassComponent construct(PlayerEntity player);
  public abstract Identifier getID();
  
  public Text getName() {
    Identifier id = getID();
    String key = "name.rpgclass." + id.getNamespace() + "." + id.getPath();
    if (I18n.hasTranslation(key)) {
      return new TranslatableText(key);
    } else {
      return new LiteralText(key);
    }
  }
  
  public List<Text> getDescription() {
    List<Text> lines = new ArrayList<>();
    Identifier id = getID();
    if (id != null) {
      int i = 0;
      while (true) {
        String key = "desc.rpgclass." + id.getNamespace() + "." + id.getPath() + "." + i;
        if (!I18n.hasTranslation(key)) break;
        lines.add(new TranslatableText(key));
        i++;
      }
    }
    return lines;
  }
  
}
