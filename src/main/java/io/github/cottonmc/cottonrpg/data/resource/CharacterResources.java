package io.github.cottonmc.cottonrpg.data.resource;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Objects;

public interface CharacterResources extends RpgDataContainer<CharacterResource, CharacterResourceEntry> {
	int SYNC_FLAG = 0b10;

	class Impl extends BaseRpgDataContainer<CharacterResource, CharacterResourceEntry> implements CharacterResources {
		@Override
		public CharacterResourceEntry giveIfAbsent(Identifier id) {
			return this.giveIfAbsent(Objects.requireNonNull(CottonRPG.RESOURCES.get(id)));
		}

		@Override
		public CharacterResourceEntry giveIfAbsent(CharacterResource resource) {
			return this.underlying.computeIfAbsent(resource, CharacterResourceEntry::new);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterResource, CharacterResourceEntry> implements CharacterResources {
		public Proxy(CharacterResources parent, @Nullable CharacterResources child) {
			super(parent, child);
		}
	}
}
