package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseRpgDataContainer<T extends RpgDataType, E extends RpgDataEntry<T>> implements RpgDataContainer<T, E> {
    protected final Map<T, E> underlying = new HashMap<>();
    private final List<T> removed = new ArrayList<>();

    @Override
    public int size() {
        return underlying.size();
    }

    @Override
    public void clear() {
        underlying.clear();
    }

    @Override
    public boolean has(T skill) {
        return underlying.containsKey(skill);
    }

    @Override
    public E get(T skill) {
        return underlying.get(skill);
    }

    @Override
    public E remove(T skill) {
        E entry = underlying.remove(skill);
        if (entry!=null) removed.add(skill);
        return entry;
    }

    @Override
    public Iterator<E> iterator() {
        return underlying.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        underlying.values().forEach(action);
    }

    @Override
    public void fromTag(CompoundTag entries) {
        for (String key : entries.getKeys()) {
            if (entries.getType(key) == NbtType.COMPOUND) try {
                Identifier id = new Identifier(key);
                this.giveIfAbsent(id).fromTag(entries.getCompound(key));
            } catch (Exception e) {
                CottonRPG.LOGGER.error("[CottonRPG] Couldn't read entry {}!", key, e);
            }
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        for (E entry : this) {
            tag.put(entry.getId().toString(), entry.toTag());
        }

        return tag;
    }

    @Override
    public boolean isDirty() {
        if (!removed.isEmpty()) return true;

        for(E entry : underlying.values()) {
            if (entry.isDirty()) return true;
        }

        return false;
    }

    @Override
    public void clearDirty() {
        this.underlying.forEach((identifier, e) -> e.clearDirty());
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf) {
        if (!removed.isEmpty()) {
            writeSyncPacket(buf, this.underlying.values(), true);
            removed.clear();
        } else {
            writeSyncPacket(buf, gatherDirtyEntries(), false);
        }
        clearDirty();
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        boolean clear = buf.readBoolean();
        int count = buf.readInt();

        if (clear) this.clear();

        for (int i = 0; i < count; i++) {
            this.giveIfAbsent(buf.readIdentifier()).readFromPacket(buf);
        }
    }

    private void writeSyncPacket(PacketByteBuf buf, Collection<E> entries, boolean clear) {
        buf.writeBoolean(clear);
        buf.writeInt(entries.size());
        for (E entry : entries) {
            buf.writeIdentifier(entry.getId());
            entry.writeToPacket(buf);
        }
    }

    private List<E> gatherDirtyEntries() {
        List<E> entries = new ArrayList<>();

        for (E entry : this) {
            if (entry.isDirty()) entries.add(entry);
        }

        return entries;
    }
}
