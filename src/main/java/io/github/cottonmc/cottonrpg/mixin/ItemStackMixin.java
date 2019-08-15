package io.github.cottonmc.cottonrpg.mixin;

import io.github.cottonmc.cottonrpg.data.CharacterDataHolder;
import io.github.cottonmc.cottonrpg.data.ItemDataHolder;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin implements CharacterDataHolder {

	@Override
	public CharacterClasses crpg_getClasses() {
		Item item = ((ItemStack)(Object)this).getItem();
		if (item instanceof ItemDataHolder) {
			return ((ItemDataHolder)item).getClasses((ItemStack)(Object)this);
		}
		return null;
	}

	@Override
	public CharacterResources crpg_getResources() {
		Item item = ((ItemStack)(Object)this).getItem();
		if (item instanceof ItemDataHolder) {
			return ((ItemDataHolder)item).getResources((ItemStack)(Object)this);
		}
		return null;
	}

	@Override
	public CharacterSkills crpg_getSkills() {
		Item item = ((ItemStack)(Object)this).getItem();
		if (item instanceof ItemDataHolder) {
			return ((ItemDataHolder)item).getSkills((ItemStack)(Object)this);
		}
		return null;
	}

	@Override
	public void crpg_setClasses(CharacterClasses classes) {
		Item item = ((ItemStack)(Object)this).getItem();
		if (item instanceof ItemDataHolder) {
			((ItemDataHolder)item).setClasses((ItemStack)(Object)this, classes);
		}
	}

	@Override
	public void crpg_setResources(CharacterResources resources) {
		Item item = ((ItemStack)(Object)this).getItem();
		if (item instanceof ItemDataHolder) {
			((ItemDataHolder)item).setResources((ItemStack)(Object)this, resources);
		}
	}

	@Override
	public void crpg_setSkills(CharacterSkills skills) {
		Item item = ((ItemStack)(Object)this).getItem();
		if (item instanceof ItemDataHolder) {
			((ItemDataHolder)item).setSkills((ItemStack)(Object)this, skills);
		}
	}
}
