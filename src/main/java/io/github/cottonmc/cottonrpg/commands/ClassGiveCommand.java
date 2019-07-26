package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterClass;
import io.github.cottonmc.cottonrpg.data.CharacterClassEntry;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ClassGiveCommand implements Command<ServerCommandSource> {

  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity e = context.getSource().getEntity();
    if (e instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) e;
      
      Identifier cid = context.getArgument("classname", Identifier.class);
      
      CharacterClass cct = CottonRPG.CLASSES.get(cid);
      
      if (cct == null) {
        Text text = new LiteralText("No such class").formatted(Formatting.RED);
        p.addChatMessage(text, false);
        return 2;
      }
      
      CharacterData.get(p).getClasses().giveIfAbsent(new CharacterClassEntry(cid));
      
      p.addChatMessage(new LiteralText("Done!").formatted(Formatting.GOLD), false);
    }
    return 1;
  }

}