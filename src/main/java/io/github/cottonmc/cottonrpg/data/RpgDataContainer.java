package io.github.cottonmc.cottonrpg.data;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface RpgDataContainer<T extends RpgDataType, E extends RpgDataEntry<T>> extends Iterable<E>, AutoSyncedComponent, ServerTickingComponent {

	int size();

	void clear();

	boolean has(T type);

	E get(T type);

	E giveIfAbsent(Identifier id);

	E giveIfAbsent(T type);

	E remove(T type);

	@Override
	void readFromNbt(CompoundTag tag);

	@Override
	void writeToNbt(CompoundTag tag);

	boolean isDirty();

	void clearDirty();

	@Override
	void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player);

	@Override
	void applySyncPacket(PacketByteBuf buf);
}
