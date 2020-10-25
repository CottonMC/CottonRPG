package io.github.cottonmc.cottonrpg.data;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.rpgskill.CharacterSkills;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public class CharacterData implements Component {
	public static final ComponentKey<CharacterData> KEY = ComponentRegistry.getOrCreate(CottonRPG.id("character_data"), CharacterData.class);

	private final CharacterClasses classes;
	private final CharacterSkills skills;
	private final CharacterResources resources;

	public CharacterData() {
		this(new CharacterClasses.Impl(), new CharacterSkills.Impl(), new CharacterResources.Impl());
	}

	protected CharacterData(CharacterClasses classes, CharacterSkills skills, CharacterResources resources) {
		this.classes = classes;
		this.skills = skills;
		this.resources = resources;
	}

	/**
	 * Get a player's data.
	 *
	 * @param player The player to get for.
	 * @return The classes, resources, and skills of the player.
	 */
	public static CharacterData get(PlayerEntity player) {
		return KEY.get(player);
	}

	/**
	 * Get a stack's data.
	 *
	 * @param stack The stack to get for.
	 * @return The classes, resources, and skills of the item.
	 */
	@Nullable
	public static CharacterData get(ItemStack stack) {
		if (CottonRPG.CCA_ITEM_LOADED) {
			return KEY.getNullable(stack);
		}
		return null;
	}

	public static CharacterData get(PlayerEntity player, ItemStack stack) {
		return mergedView(CharacterData.get(player), CharacterData.get(stack));
	}

	public static ProxyCharacterData mergedView(CharacterData parent, CharacterData child) {
		return new ProxyCharacterData(parent, child);
	}

	public CharacterClasses getClasses() {
		return this.classes;
	}

	public CharacterResources getResources() {
		return this.resources;
	}

	public CharacterSkills getSkills() {
		return this.skills;
	}

	@Override
	public void readFromNbt(CompoundTag crpg) {
		if (crpg.contains("Classes")) {
			this.classes.fromTag(crpg.getCompound("Classes"));
		}

		if (crpg.contains("Resources")) {
			this.resources.fromTag(crpg.getCompound("Resources"));
		}

		if (crpg.contains("Skills")) {
			this.skills.fromTag(crpg.getCompound("Skills"));
		}
	}

	@Override
	public void writeToNbt(CompoundTag crpg) {
		crpg.put("Classes", classes.toTag());
		crpg.put("Resources", resources.toTag());
		crpg.put("Skills", skills.toTag());
	}
}
