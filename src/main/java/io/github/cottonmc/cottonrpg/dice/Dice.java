package io.github.cottonmc.cottonrpg.dice;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dice {
	//TODO: multiple dice? operations other than addition?
	public static final Pattern PATTERN = Pattern.compile("(?<count>\\d+)\\s*d(?<sides>\\d+)\\s*(?:\\+(?<bonus>\\d+(?!d)))?");

	/**
	 * Roll a set of dice.
	 * @param formula A D&D-style dice-roll formula, ex. 1d20+8.
	 *                Only about 50% implemented currently.
	 *                Rolling multiple dice types at once and using modifiers other than adding are not supported.
	 * @see <a href="https://en.wikipedia.org/wiki/Dice_notation">Dice notation</a>
	 * @return a RollResult of what was calculated.
	 */
	public static RollResult roll(String formula) {
		Random rand = new Random();
		Matcher matcher = PATTERN.matcher(formula);
		RollResult res = new RollResult();
		while (matcher.find()) {
			int rolls = Integer.parseInt(matcher.group("count"));
			if (rolls < 1) throw new IllegalArgumentException("Must roll at least one die!");
			for (int i = 0; i < rolls; i++) {
				int sides = Integer.parseInt(matcher.group("sides"));
				if (sides < 1) throw new IllegalArgumentException("Die must have at least one side!");
				int roll = rand.nextInt(sides) + 1;
				if (roll == 1) res.fail();
				res.addNatural(roll);
				res.addToTotal(roll);
			}
			res.addToTotal(Integer.parseInt(matcher.group("bonus")));
		}
		return res;
	}
}
