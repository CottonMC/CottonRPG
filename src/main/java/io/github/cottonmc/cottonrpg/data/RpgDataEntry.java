package io.github.cottonmc.cottonrpg.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface RpgDataEntry<T extends RpgDataType> {
	Identifier getId();

	T getType();

	CompoundTag toTag();

	void fromTag(CompoundTag tag);

	void markDirty();

	boolean isDirty();

	void clearDirty();

	void writeToPacket(PacketByteBuf buf);

	void readFromPacket(PacketByteBuf buf);
}
