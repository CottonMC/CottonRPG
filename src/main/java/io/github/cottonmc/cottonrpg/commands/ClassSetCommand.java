package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClassEntry;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClass;
import io.github.cottonmc.cottonrpg.data.CharacterData;
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
		Entity entity = context.getSource().getEntity();
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			if (!player.allowsPermissionLevel(4)) { return 2; }

			Identifier id = context.getArgument("classname", Identifier.class);

			CharacterClass clazz = CottonRPG.CLASSES.get(id);

			if (clazz == null) {
				Text text = new TranslatableText("No such class").formatted(Formatting.RED);
				player.addChatMessage(text, false);
				return 2;
			}

			CharacterClassEntry entry = CharacterData.get(player).getClasses().get(id);

			if (entry == null) {
				Text text = new TranslatableText("Class is not enabled").formatted(Formatting.LIGHT_PURPLE);
				player.addChatMessage(text, false);
				return 2;
			}

			int level = context.getArgument("level", Integer.class);

			entry.setLevel(level);

			Text text = new TranslatableText(id.toString() + " <- " + level).formatted(Formatting.GOLD);

			player.addChatMessage(text, false);
		}
		return 1;
	}

}
