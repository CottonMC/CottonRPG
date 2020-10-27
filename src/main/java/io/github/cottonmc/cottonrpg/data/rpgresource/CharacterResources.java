package io.github.cottonmc.cottonrpg.data.rpgresource;

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

public interface CharacterResources extends RpgDataContainer<CharacterResource, CharacterResourceEntry> {

	/**
	 * Get a player's data.
	 *
	 * @param player The player to get for.
	 * @return The resources of the player.
	 */
	static CharacterResources get(PlayerEntity player) {
		return RpgComponents.RESOURCES.get(player);
	}

	/**
	 * Get a stack's data.
	 *
	 * @param stack The stack to get for.
	 * @return The resources of the item.
	 */
	@Nullable
	static CharacterResources get(ItemStack stack) {
		if (CottonRPG.CCA_ITEM_LOADED) {
			return RpgComponents.RESOURCES.getNullable(stack);
		}
		return null;
	}

	static CharacterResources get(PlayerEntity player, ItemStack stack) {
		return new Proxy(CharacterResources.get(player), CharacterResources.get(stack));
	}

	class Impl extends BaseRpgDataContainer<CharacterResource, CharacterResourceEntry> implements CharacterResources {
		public Impl(Object holder) {
			super(holder);
		}

		@Override
		public CharacterResourceEntry giveIfAbsent(Identifier id) {
			return this.giveIfAbsent(Objects.requireNonNull(CottonRPG.RESOURCES.get(id)));
		}

		@Override
		public CharacterResourceEntry giveIfAbsent(CharacterResource resource) {
			return this.underlying.computeIfAbsent(resource, CharacterResourceEntry::new);
		}

		@Override
		public void serverTick() {
			this.trySync(RpgComponents.RESOURCES);
			this.forEach(CharacterResourceEntry::tick);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterResource, CharacterResourceEntry> implements CharacterResources {
		public Proxy(CharacterResources parent, @Nullable CharacterResources child) {
			super(parent, child);
		}
	}
}
