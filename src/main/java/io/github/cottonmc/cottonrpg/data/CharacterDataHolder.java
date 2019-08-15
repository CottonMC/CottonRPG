package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;

/**
 * Internal class to save character data onto an object.
 */
public interface CharacterDataHolder {
	CharacterClasses crpg_getClasses();
	CharacterResources crpg_getResources();
	CharacterSkills crpg_getSkills();
	void crpg_setClasses(CharacterClasses classes);
	void crpg_setResources(CharacterResources resources);
	void crpg_setSkills(CharacterSkills skills);
}
