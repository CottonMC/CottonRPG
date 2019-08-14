package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class CharacterSkillEntry {
	public final Identifier id;
	private CharacterSkill skill;
	private int cooldown = 0;
	private transient boolean dirty = false;

	public CharacterSkillEntry(Identifier id) {
		this.id = id;
		this.skill = CottonRPG.SKILLS.get(id);
	}

	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("cooldown", this.cooldown);
		return tag;
	}

	public CharacterSkillEntry fromTag(CompoundTag tag) {
		this.cooldown = tag.getInt("cooldown");
		markDirty();
		return this;
	}

	public int getCooldown() {
		return this.cooldown;
	}

	public void setCooldown(int i) {
		markDirty();
		this.cooldown = i;
	}

	public void startCooldown() {
		setCooldown(skill.getCooldownTime());
	}

	public void tick() {
		skill.tick(this);
	}

	public void markDirty() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clearDirty() {
		dirty = false;
	}
}
