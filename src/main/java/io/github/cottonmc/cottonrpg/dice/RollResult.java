package io.github.cottonmc.cottonrpg.dice;

import io.github.cottonmc.cottonrpg.CottonRPG;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of a dice roll.
 * Stores the total sum of the rolls, the natural rolls, and whether there was any critical failure.
 */
public class RollResult {
	private int total;
	private List<Integer> naturals;
	private boolean critFail;

	public RollResult() {
		this(0, new ArrayList<>(), false);
	}

	public RollResult(int total, List<Integer> naturals, boolean critFail) {
		this.total = total;
		this.naturals = naturals;
		this.critFail = critFail;
	}

	public int getTotal() {
		return total;
	}

	public void addToTotal(int amount) {
		total += amount;
	}

	public List<Integer> getNaturals() {
		return naturals;
	}

	public void addNatural(int amount) {
		naturals.add(amount);
	}

	public boolean isCritFail() {
		if (!CottonRPG.config.haveCriticalFailures) return false;
		return critFail;
	}

	public void setCritFail(boolean critFail) {
		this.critFail = critFail;
	}

	public void fail() {
		critFail = true;
	}

	public String getFormattedNaturals() {
		String text = naturals.toString();
		return text.substring(1, text.length()-1);
	}
}
