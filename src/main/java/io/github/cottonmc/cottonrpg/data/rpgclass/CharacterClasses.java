package io.github.cottonmc.cottonrpg.data.rpgclass;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.RpgComponents;
import io.github.cottonmc.cottonrpg.data.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Objects;

public interface CharacterClasses extends RpgDataContainer<CharacterClass, CharacterClassEntry> {

	/**
	 * Get a player's data.
	 *
	 * @param player The player to get for.
	 * @return The classes of the player.
	 */
	static CharacterClasses get(PlayerEntity player) {
		return RpgComponents.CLASSES.get(player);
	}

	/**
	 * Get a stack's data.
	 *
	 * @param stack The stack to get for.
	 * @return The classes of the item.
	 */
	@Nullable
	static CharacterClasses get(ItemStack stack) {
		if (CottonRPG.CCA_ITEM_LOADED) {
			return RpgComponents.CLASSES.getNullable(stack);
		}
		return null;
	}

	static CharacterClasses get(PlayerEntity player, ItemStack stack) {
		return new Proxy(CharacterClasses.get(player), CharacterClasses.get(stack));
	}

	class Impl extends BaseRpgDataContainer<CharacterClass, CharacterClassEntry> implements CharacterClasses {
		public Impl(Object holder) {
			super(holder);
		}

		@Override
		public CharacterClassEntry giveIfAbsent(Identifier id) {
			return this.giveIfAbsent(Objects.requireNonNull(CottonRPG.CLASSES.get(id)));
		}

		@Override
		public CharacterClassEntry giveIfAbsent(CharacterClass clazz) {
			return this.underlying.computeIfAbsent(clazz, CharacterClassEntry::new);
		}

		@Override
		public void serverTick() {
			this.trySync(RpgComponents.CLASSES);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterClass, CharacterClassEntry> implements CharacterClasses {
		public Proxy(CharacterClasses parent, @Nullable CharacterClasses child) {
			super(parent, child);
		}
	}
}
