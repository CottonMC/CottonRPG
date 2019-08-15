package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Interface for letting items hold clazz, resource, or skill.
 */
public interface ItemDataHolder {
	@Nullable
	CharacterClasses getClasses(ItemStack stack);

	@Nullable
	CharacterResources getResources(ItemStack stack);

	@Nullable
	CharacterSkills getSkills(ItemStack stack);

	void setClasses(ItemStack stack, CharacterClasses classes);

	void setResources(ItemStack stack, CharacterResources resources);

	void setSkills(ItemStack stack, CharacterSkills skills);
}
