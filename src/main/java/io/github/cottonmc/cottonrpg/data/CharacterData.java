package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class CharacterData {
	private CharacterDataHolder holder;

	/**
	 * Get a player's data.
	 * @param player The player to get for.
	 * @return The classes, resources, and skills of the player.
	 */
	public static CharacterData get(PlayerEntity player) {
		CharacterData data = new CharacterData();
		data.holder = ((CharacterDataHolder)player);
		return data;
	}

	/**
	 * Get a stack's data.
	 * @param stack The stack to get for.
	 * @return
	 */
	@Nullable
	public static CharacterData get(ItemStack stack) {
		CharacterData data = new CharacterData();
		data.holder = ((CharacterDataHolder)(Object)stack);
		return data;
	}

	public static CharacterData get(PlayerEntity player, ItemStack stack) {
		return new ProxyCharData(CharacterData.get(player), CharacterData.get(stack));
	}

	public CharacterClasses getClasses() {
		return holder.crpg_getClasses();
	}

	public CharacterResources getResources() {
		return holder.crpg_getResources();
	}

	public CharacterSkills getSkills() {
		return holder.crpg_getSkills();
	}

}
