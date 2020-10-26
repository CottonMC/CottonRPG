package io.github.cottonmc.cottonrpg.data.rpgskill;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.RpgComponents;
import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Objects;

public interface CharacterSkills extends RpgDataContainer<CharacterSkill, CharacterSkillEntry> {

	/**
	 * Get a player's data.
	 *
	 * @param player The player to get for.
	 * @return The skills of the player.
	 */
	static CharacterSkills get(PlayerEntity player) {
		return RpgComponents.SKILLS.get(player);
	}

	/**
	 * Get a stack's data.
	 *
	 * @param stack The stack to get for.
	 * @return The skills of the item.
	 */
	@Nullable
	static CharacterSkills get(ItemStack stack) {
		if (CottonRPG.CCA_ITEM_LOADED) {
			return RpgComponents.SKILLS.getNullable(stack);
		}
		return null;
	}

	static CharacterSkills get(PlayerEntity player, ItemStack stack) {
		return new Proxy(CharacterSkills.get(player), CharacterSkills.get(stack));
	}

	class Impl extends BaseRpgDataContainer<CharacterSkill, CharacterSkillEntry> implements CharacterSkills {
		public Impl(Object holder) {
			super(holder);
		}

		@Override
		public CharacterSkillEntry giveIfAbsent(Identifier id) {
			return this.underlying.computeIfAbsent(Objects.requireNonNull(CottonRPG.SKILLS.get(id)), CharacterSkillEntry::new);
		}

		@Override
		public CharacterSkillEntry giveIfAbsent(CharacterSkill skill) {
			return this.underlying.computeIfAbsent(skill, CharacterSkillEntry::new);
		}

		@Override
		public void serverTick() {
			this.trySync(RpgComponents.SKILLS);
			this.forEach(CharacterSkillEntry::tick);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterSkill, CharacterSkillEntry> implements CharacterSkills {
		public Proxy(CharacterSkills parent, @Nullable CharacterSkills child) {
			super(parent, child);
		}
	}
}
