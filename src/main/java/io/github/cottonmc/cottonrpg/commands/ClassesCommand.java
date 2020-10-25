package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClassEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class ClassesCommand implements Command<ServerCommandSource> {

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Entity entity = context.getSource().getEntity();
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			Text header = new TranslatableText("cmd.cottonrpg.classes.yourclasses").formatted(Formatting.AQUA);
			player.sendMessage(header, false);

			for (CharacterClassEntry entry : CharacterData.get(player).getClasses()) {
				String name = entry.getType().getName().asString();
				Text text = new TranslatableText("cmd.cottonrpg.classes.entry", name, entry.getId().toString(), entry.getLevel()).formatted(Formatting.GOLD);
				player.sendMessage(text, false);
			}
		}
		return 1;
	}

}
