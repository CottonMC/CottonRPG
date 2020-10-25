package io.github.cottonmc.cottonrpg.data.clazz;

import io.github.cottonmc.cottonrpg.data.RpgDataEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/**
 * Flyweight for interacting with clazz.
 * A CharacterClassEntry stores the Identifier of the class, the player's level of the class, and how much XP in the class the player has.
 * Class XP is separate from a player's XP, and the XP in other classes.
 * However, you may want to implement experience on your classes, so feel free to access this then.
 */
public class CharacterClassEntry implements RpgDataEntry<CharacterClass> {
	public final Identifier id;
	private final CharacterClass clazz;
	private int level = 0;
	private int experience = 0;
	private transient boolean dirty = false;

	public CharacterClassEntry(CharacterClass clazz) {
		this.id = clazz.getId();
		this.clazz = clazz;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public CharacterClass getType() {
		return clazz;
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("level", this.level);
		tag.putInt("experience", this.experience);
		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.level = tag.getInt("level");
		this.experience = tag.getInt("experience");
		markDirty();
	}

	@Override
	public void writeToPacket(PacketByteBuf buf) {
		buf.writeInt(this.getLevel());
		buf.writeInt(this.getExperience());
	}

	@Override
	public void readFromPacket(PacketByteBuf buf) {
		this.setLevel(buf.readInt());
		this.setExperience(buf.readInt());
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int i) {
		markDirty();
		this.level = i;
	}

	public int getExperience() {
		return this.experience;
	}

	public void setExperience(int i) {
		markDirty();
		this.experience = i;
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
}
