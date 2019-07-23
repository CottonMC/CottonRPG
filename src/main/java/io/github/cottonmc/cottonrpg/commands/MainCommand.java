package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MainCommand implements Command<ServerCommandSource> {

  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity e = context.getSource().getEntity();
    if (e instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) e;
      
      // int level = CottonRPG.DEMO_CLASS.maybeGet(p).map(IClassComponent::getLevel).orElse(-1);
      
      Text text = new net.minecraft.text.TranslatableText("Hello! Actual help should be provided here, but handi is too lazy ;(");
      
      p.addChatMessage(text, false);
    }
    return 1;
  }

}
