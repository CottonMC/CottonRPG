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
import net.minecraft.text.LiteralText;
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
      CharacterData.get(player).getClasses().forEach((id, entry) -> {
        String name = CottonRPG.CLASSES.get(id).getName().asString();
        Text text = new TranslatableText("cmd.cottonrpg.classes.entry", name, id.toString(), entry.getLevel()).formatted(Formatting.GOLD);
        player.addChatMessage(text, false);
      });
    }
    return 1;
  }

}
