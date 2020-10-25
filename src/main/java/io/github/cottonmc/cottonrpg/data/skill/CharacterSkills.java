package io.github.cottonmc.cottonrpg.data.skill;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Objects;

public interface CharacterSkills extends RpgDataContainer<CharacterSkill, CharacterSkillEntry> {
	int SYNC_FLAG = 0b100;

	class Impl extends BaseRpgDataContainer<CharacterSkill, CharacterSkillEntry> implements CharacterSkills {
		@Override
		public CharacterSkillEntry giveIfAbsent(Identifier id) {
			return this.underlying.computeIfAbsent(Objects.requireNonNull(CottonRPG.SKILLS.get(id)), CharacterSkillEntry::new);
		}

		@Override
		public CharacterSkillEntry giveIfAbsent(CharacterSkill skill) {
			return this.underlying.computeIfAbsent(skill, CharacterSkillEntry::new);
		}
	}

	class Proxy extends ProxyRpgDataContainer<CharacterSkill, CharacterSkillEntry> implements CharacterSkills {
		public Proxy(CharacterSkills parent, @Nullable CharacterSkills child) {
			super(parent, child);
		}
	}
}
