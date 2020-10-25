package io.github.cottonmc.cottonrpg.data.resource;

import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public interface CharacterResources extends RpgDataContainer<CharacterResource, CharacterResourceEntry> {
	int SYNC_FLAG = 0b10;

	@Override
	default int getSyncFlag() {
		return SYNC_FLAG;
	}

    class Impl extends BaseRpgDataContainer<CharacterResource, CharacterResourceEntry> implements CharacterResources {
		@Override
		public CharacterResourceEntry giveIfAbsent(Identifier id) {
			return this.underlying.computeIfAbsent(id, CharacterResourceEntry::new);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterResource, CharacterResourceEntry> implements CharacterResources {
		public Proxy(CharacterResources parent, @Nullable CharacterResources child) {
			super(parent, child);
		}
	}
}
