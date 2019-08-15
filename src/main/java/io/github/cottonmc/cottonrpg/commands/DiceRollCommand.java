package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.cottonrpg.dice.Dice;
import io.github.cottonmc.cottonrpg.dice.RollResult;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class DiceRollCommand implements Command<ServerCommandSource> {
	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String formula = context.getArgument("formula", String.class);
		RollResult result;
		try {
			result = Dice.roll(formula);
		} catch (IllegalArgumentException e) {
			context.getSource().sendError(new LiteralText(e.getMessage()));
			return -1;
		}
		if (result.isCritFail()) context.getSource().sendFeedback(new TranslatableText("msg.cottonrpg.roll.fail", result.getFormattedNaturals()), false);
		else context.getSource().sendFeedback(new TranslatableText("msg.cottonrpg.roll.result", result.getTotal(), result.getFormattedNaturals()), false);
		return 1;
	}
}
