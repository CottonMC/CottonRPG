package io.github.cottonmc.cottonrpg.data.clazz;

import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public interface CharacterClasses extends RpgDataContainer<CharacterClass, CharacterClassEntry> {
	int SYNC_FLAG = 0b1;

	@Override
	default int getSyncFlag() {
		return SYNC_FLAG;
	}

	class Impl extends BaseRpgDataContainer<CharacterClass, CharacterClassEntry> implements CharacterClasses {
		@Override
		public CharacterClassEntry giveIfAbsent(Identifier id) {
			return this.underlying.computeIfAbsent(id, CharacterClassEntry::new);
		}
	}


	class Proxy extends ProxyRpgDataContainer<CharacterClass, CharacterClassEntry> implements CharacterClasses {
		public Proxy(CharacterClasses parent, @Nullable CharacterClasses child) {
			super(parent, child);
		}
	}
}
