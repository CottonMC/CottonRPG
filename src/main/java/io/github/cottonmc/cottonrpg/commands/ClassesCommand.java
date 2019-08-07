package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterClassEntry;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ClassesCommand implements Command<ServerCommandSource> {

  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity entity = context.getSource().getEntity();
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      Text header = new TranslatableText("cmd.cottonrpg.classes.yourclasses").formatted(Formatting.AQUA);
      player.addChatMessage(header, false);
      CharacterData data = CharacterData.get(player);
      CottonRPG.CLASSES.forEach(clazz -> {
        String name = clazz.getName().asString();
        Identifier id = clazz.getId();
        CharacterClassEntry entry = data.getClasses().get(id);
        if (entry != null) {
          Text text = new TranslatableText("%s [%s] == %d", name, id.toString(), entry.getLevel()).formatted(Formatting.GOLD);
          player.addChatMessage(text, false);
        }
      });
      /*
      CharacterData.get(p).getClasses().forEach((id, ce) -> {
        String name = CottonRPG.CLASSES.get(id).getName().asString();
        
      });
      */
    }
    return 1;
  }

}
