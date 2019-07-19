package io.github.cottonmc.cottonrpgcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.CottonRPGMod;
import io.github.cottonmc.cottonrpg.components.IClassComponent;
import io.github.cottonmc.cottonrpg.demo.DemoClass;
import nerdhub.cardinal.components.api.ComponentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MainCommand implements Command<ServerCommandSource> {

  public MainCommand() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity e = context.getSource().getEntity();
    if (e instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) e;
      
      int level = CottonRPGMod.DEMO_CLASS.maybeGet(p).map(IClassComponent::getLevel).orElse(-1);
      
      Text text = new net.minecraft.text.TranslatableText("Hello! " + level);
      
      p.addChatMessage(text, false);
    }
    return 1;
  }

}
