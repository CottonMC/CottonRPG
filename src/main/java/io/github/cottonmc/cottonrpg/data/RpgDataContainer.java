package io.github.cottonmc.cottonrpg.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public interface RpgDataContainer<T extends RpgDataType, E extends RpgDataEntry<T>> {

    int size();

    void clear();

    boolean has(T skill);

    E get(T skill);

    E giveIfAbsent(Identifier id);

    E giveIfAbsent(T type);

    E remove(T skill);

    void forEach(BiConsumer<T, E> consumer);

    void fromTag(CompoundTag tag);

    CompoundTag toTag();

    boolean isDirty();

    void clearDirty();

    void writeSyncPacket(PacketByteBuf buf);

    void applySyncPacket(PacketByteBuf buf);
}
