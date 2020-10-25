package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResource;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResourceEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ResourceGiveCommand implements Command<ServerCommandSource> {

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Entity entity = context.getSource().getEntity();
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			Identifier id = context.getArgument("resourcename", Identifier.class);

			CharacterResource resource = CottonRPG.RESOURCES.get(id);

			if (resource == null) {
				Text text = new LiteralText("No such resource").formatted(Formatting.RED);
				player.sendMessage(text, false);
				return 2;
			}

			CharacterData.get(player).getResources().giveIfAbsent(new CharacterResourceEntry(id));

			player.sendMessage(new LiteralText("Done!").formatted(Formatting.GOLD), false);
		}
		return 1;
	}

}
