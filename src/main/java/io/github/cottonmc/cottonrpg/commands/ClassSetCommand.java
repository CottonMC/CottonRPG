package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.ClassRegistry;
import io.github.cottonmc.cottonrpg.components.IClassComponent;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ClassSetCommand implements Command<ServerCommandSource> {

  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity e = context.getSource().getEntity();
    if (e instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) e;
      if (!p.allowsPermissionLevel(4)) { return 2; }
      
      Identifier cid = context.getArgument("classname", Identifier.class);
      
      ComponentType<IClassComponent> cc = ClassRegistry.get(cid);
      
      if (cc == null) {
        Text text = new TranslatableText("No such class").formatted(Formatting.RED);
        p.addChatMessage(text, false);
        return 2;
      }
      
      IClassComponent c = cc.get(p);
      
      int level = context.getArgument("level", Integer.class);
      
      c.setLevel(level);
      
      Text text = new TranslatableText(cid.toString() + " <- " + level).formatted(Formatting.GOLD);
      
      p.addChatMessage(text, false);
    }
    return 1;
  }
  
}
