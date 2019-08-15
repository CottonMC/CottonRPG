package io.github.cottonmc.cottonrpg.util;

import io.github.cottonmc.cottonrpg.data.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.CharacterResources;
import io.github.cottonmc.cottonrpg.data.CharacterSkills;

public interface CharacterDataHolder {
	CharacterClasses crpg_getClasses();
	CharacterResources crpg_getResources();
	CharacterSkills crpg_getSkills();
	void crpg_setClasses(CharacterClasses classes);
	void crpg_setResources(CharacterResources resources);
	void crpg_setSkills(CharacterSkills skills);
}
