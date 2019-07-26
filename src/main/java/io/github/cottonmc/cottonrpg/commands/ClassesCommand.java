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
    Entity e = context.getSource().getEntity();
    if (e instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) e;
      Text header = new TranslatableText("cmd.cottonrpg.classes.yourclasses").formatted(Formatting.AQUA);
      p.addChatMessage(header, false);
      CharacterData cd = CharacterData.get(p);
      CottonRPG.CLASSES.forEach(cc -> {
        String name = cc.getName().asString();
        Identifier cid = cc.getID();
        CharacterClassEntry ce = cd.getClasses().get(cid);
        if (ce != null) {
          Text t = new TranslatableText("%s [%s] == %d", name, cid.toString(), ce.getLevel()).formatted(Formatting.GOLD);
          p.addChatMessage(t, false);
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
