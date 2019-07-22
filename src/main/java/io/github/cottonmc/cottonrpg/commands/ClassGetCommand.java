package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.ClassRegistry;
import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.ClassComponentType;
import io.github.cottonmc.cottonrpg.util.RPGPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ClassGetCommand implements Command<ServerCommandSource> {
  
  @Override
  public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity e = context.getSource().getEntity();
    if (e instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) e;
      
      Identifier cid = context.getArgument("classname", Identifier.class);
      
      ClassComponentType cct = ClassRegistry.INSTANCE.get(cid);
      
      if (cct == null) {
        Text text = new TranslatableText("No such class").formatted(Formatting.RED);
        p.addChatMessage(text, false);
        return 2;
      }
      
      ClassComponent cc = ((RPGPlayer) p).cottonRPGGetCharacterDataHolder().classes.get(cid);
      
      if (cc == null) {
        Text text = new TranslatableText("Class is not enabled").formatted(Formatting.LIGHT_PURPLE);
        p.addChatMessage(text, false);
        return 2;
      }
      
      Text text = new TranslatableText(cid.toString() + " == " + cc.getLevel()).formatted(Formatting.GOLD);
      
      p.addChatMessage(text, false);
    }
    return 1;
  }

}
