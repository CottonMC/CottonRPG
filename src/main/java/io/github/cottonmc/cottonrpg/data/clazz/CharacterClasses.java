package io.github.cottonmc.cottonrpg.data.clazz;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Objects;

public interface CharacterClasses extends RpgDataContainer<CharacterClass, CharacterClassEntry> {
	int SYNC_FLAG = 0b1;

    class Impl extends BaseRpgDataContainer<CharacterClass, CharacterClassEntry> implements CharacterClasses {
		@Override
		public CharacterClassEntry giveIfAbsent(Identifier id) {
			return this.giveIfAbsent(Objects.requireNonNull(CottonRPG.CLASSES.get(id)));
		}

		@Override
		public CharacterClassEntry giveIfAbsent(CharacterClass clazz) {
			return this.underlying.computeIfAbsent(clazz, CharacterClassEntry::new);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterClass, CharacterClassEntry> implements CharacterClasses {
		public Proxy(CharacterClasses parent, @Nullable CharacterClasses child) {
			super(parent, child);
		}
	}
}
