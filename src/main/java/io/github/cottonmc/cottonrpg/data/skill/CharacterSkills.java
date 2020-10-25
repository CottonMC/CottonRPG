package io.github.cottonmc.cottonrpg.data.skill;

import io.github.cottonmc.cottonrpg.data.BaseRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.ProxyRpgDataContainer;
import io.github.cottonmc.cottonrpg.data.RpgDataContainer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public interface CharacterSkills extends RpgDataContainer<CharacterSkill, CharacterSkillEntry> {
    int SYNC_FLAG = 0b100;

    @Override
    default int getSyncFlag() {
        return SYNC_FLAG;
    }

    class Impl extends BaseRpgDataContainer<CharacterSkill, CharacterSkillEntry> implements CharacterSkills {
        @Override
        public CharacterSkillEntry giveIfAbsent(Identifier id) {
            return this.underlying.computeIfAbsent(id, CharacterSkillEntry::new);
        }
    }

    class Proxy extends ProxyRpgDataContainer<CharacterSkill, CharacterSkillEntry> implements CharacterSkills {
        public Proxy(CharacterSkills parent, @Nullable CharacterSkills child) {
            super(parent, child);
        }
    }
}
