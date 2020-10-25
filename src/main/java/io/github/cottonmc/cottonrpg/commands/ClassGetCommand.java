package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClass;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClassEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ClassGetCommand implements Command<ServerCommandSource> {

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Entity entity = context.getSource().getEntity();
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			Identifier id = context.getArgument("classname", Identifier.class);

			CharacterClass clazz = CottonRPG.CLASSES.get(id);

			if (clazz == null) {
				Text text = new LiteralText("No such class").formatted(Formatting.RED);
				player.sendMessage(text, false);
				return 2;
			}

			CharacterClassEntry entry = CharacterData.get(player).getClasses().get(clazz);

			if (entry == null) {
				Text text = new LiteralText("Class is not enabled").formatted(Formatting.LIGHT_PURPLE);
				player.sendMessage(text, false);
				return 2;
			}

			Text text = new LiteralText(id.toString() + " == " + entry.getLevel()).formatted(Formatting.GOLD);

			player.sendMessage(text, false);
		}
		return 1;
	}

}
