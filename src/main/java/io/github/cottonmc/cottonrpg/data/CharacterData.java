package io.github.cottonmc.cottonrpg.data;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClassEntry;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResourceEntry;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkillEntry;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public class CharacterData implements Component {
	public static final ComponentKey<CharacterData> KEY = ComponentRegistry.getOrCreate(CottonRPG.id("character_data"), CharacterData.class);

	private final CharacterClasses classes;
	private final CharacterSkills skills;
	private final CharacterResources resources;

	public CharacterData() {
		this(new CharacterClasses(), new CharacterSkills(), new CharacterResources());
	}

	protected CharacterData(CharacterClasses classes, CharacterSkills skills, CharacterResources resources) {
		this.classes = classes;
		this.skills = skills;
		this.resources = resources;
	}

	/**
	 * Get a player's data.
	 * @param player The player to get for.
	 * @return The classes, resources, and skills of the player.
	 */
	public static CharacterData get(PlayerEntity player) {
		return KEY.get(player);
	}

	/**
	 * Get a stack's data.
	 * @param stack The stack to get for.
	 * @return
	 */
	@Nullable
	public static CharacterData get(ItemStack stack) {
		if (CottonRPG.CCA_ITEM_LOADED) {
			return KEY.getNullable(stack);
		}
		return null;
	}

	public static CharacterData get(PlayerEntity player, ItemStack stack) {
		return new ProxyCharacterData(CharacterData.get(player), CharacterData.get(stack));
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
			CompoundTag cclasses = crpg.getCompound("Classes");
			for (String key : cclasses.getKeys()) {
				if (cclasses.getType(key) == NbtType.COMPOUND) try {
					Identifier id = new Identifier(key);
					this.classes.giveIfAbsent(new CharacterClassEntry(id));
					CharacterClassEntry entry = this.classes.get(id);
					CompoundTag cclass = cclasses.getCompound(key);
					entry.fromTag(cclass);
				} catch (Exception e) {
					CottonRPG.LOGGER.error("[CottonRPG] Couldn't read class!", e);
				}
			}
		}

		if (crpg.contains("Resources")) {
			CompoundTag cresources = crpg.getCompound("Resources");
			for (String key : cresources.getKeys()) {
				if (cresources.getType(key) == NbtType.COMPOUND) try {
					Identifier id = new Identifier(key);
					this.resources.giveIfAbsent(new CharacterResourceEntry(id));
					CharacterResourceEntry entry = this.resources.get(id);
					CompoundTag cresourceBar = cresources.getCompound(key);
					entry.fromTag(cresourceBar);
				} catch (Exception e) {
					CottonRPG.LOGGER.error("[CottonRPG] Couldn't read resource!", e);
				}
			}
		}

		if (crpg.contains("Skills")) {
			CompoundTag cskills = crpg.getCompound("Skills");
			for (String key : cskills.getKeys()) {
				if (cskills.getType(key) == NbtType.COMPOUND) try {
					Identifier id = new Identifier(key);
					this.skills.giveIfAbsent(new CharacterSkillEntry(id));
					CharacterSkillEntry entry = this.skills.get(id);
					CompoundTag cskill = cskills.getCompound(key);
					entry.fromTag(cskill);
				} catch (Exception e) {
					CottonRPG.LOGGER.error("[CottonRPG] Couldn't read skill!", e);
				}
			}
		}
	}

	@Override
	public void writeToNbt(CompoundTag crpg) {
		CompoundTag cclasses = new CompoundTag();
		classes.forEach((id, entry) -> {
			CompoundTag cclass = entry.toTag();
			cclasses.put(id.toString(), cclass);
		});
		crpg.put("Classes", cclasses);

		CompoundTag cresourceBars = new CompoundTag();
		resources.forEach((id, entry) -> {
			CompoundTag cresourceBar = entry.toTag();
			cresourceBars.put(id.toString(), cresourceBar);
		});
		crpg.put("Resources", cresourceBars);

		CompoundTag cskills = new CompoundTag();
		skills.forEach((id, entry) -> {
			CompoundTag cskill = entry.toTag();
			cskills.put(id.toString(), cskill);
		});
		crpg.put("Skills", cskills);
	}
}
