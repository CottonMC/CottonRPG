package io.github.cottonmc.cottonrpg.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public interface RpgDataContainer<T extends RpgDataType, E extends RpgDataEntry<T>> {
    int getSyncFlag();

    int size();

    void clear();

    boolean has(T skill);

    boolean has(Identifier id);

    E get(T skill);

    E get(Identifier id);

    E giveIfAbsent(Identifier id);

    E remove(T skill);

    E remove(Identifier id);

    void forEach(BiConsumer<Identifier, E> consumer);

    void fromTag(CompoundTag tag);

    CompoundTag toTag();

    boolean isDirty();

    void clearDirty();

    void writeSyncPacket(PacketByteBuf buf);

    void applySyncPacket(PacketByteBuf buf);
}
