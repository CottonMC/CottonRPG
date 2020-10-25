package io.github.cottonmc.cottonrpg.data.skill;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.RpgDataEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class CharacterSkillEntry implements RpgDataEntry<CharacterSkill> {
	public final Identifier id;
	private final CharacterSkill skill;
	private int cooldown = 0;
	private transient boolean dirty = false;

	public CharacterSkillEntry(CharacterSkill skill) {
		this.skill = skill;
		this.id = skill.getId();
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public CharacterSkill getType() {
		return this.skill;
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("cooldown", this.cooldown);
		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.cooldown = tag.getInt("cooldown");
		markDirty();
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

	@Override
	public void markDirty() {
		dirty = true;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void clearDirty() {
		dirty = false;
	}

	@Override
	public void writeToPacket(PacketByteBuf buf) {
		buf.writeInt(this.getCooldown());
	}

	@Override
	public void readFromPacket(PacketByteBuf buf) {
		this.setCooldown(buf.readInt());
	}
}
