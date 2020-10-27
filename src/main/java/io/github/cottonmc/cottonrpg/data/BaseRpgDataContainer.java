package io.github.cottonmc.cottonrpg.data;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.RpgComponents;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseRpgDataContainer<T extends RpgDataType, E extends RpgDataEntry<T>> implements RpgDataContainer<T, E> {
	protected final Map<T, E> underlying = new HashMap<>();
	private final List<T> removed = new ArrayList<>();
	protected final Object holder;

	public BaseRpgDataContainer(Object holder) {
		this.holder = holder;
	}

	@Override
	public int size() {
		return underlying.size();
	}

	@Override
	public void clear() {
		underlying.clear();
	}

	@Override
	public boolean has(T type) {
		return underlying.containsKey(type);
	}

	@Override
	public E get(T type) {
		return underlying.get(type);
	}

	@Override
	public E remove(T type) {
		E entry = underlying.remove(type);
		if (entry != null) removed.add(type);
		return entry;
	}

	@Override
	public @NotNull Iterator<E> iterator() {
		return underlying.values().iterator();
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		underlying.values().forEach(action);
	}

	@Override
	public void readFromNbt(CompoundTag entries) {
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
	public void writeToNbt(CompoundTag tag) {
		for (E entry : this) {
			tag.put(entry.getId().toString(), entry.toTag());
		}
	}

	@Override
	public boolean isDirty() {
		if (!removed.isEmpty()) return true;

		for (E entry : underlying.values()) {
			if (entry.isDirty()) return true;
		}

		return false;
	}

	@Override
	public void clearDirty() {
		this.underlying.forEach((identifier, e) -> e.clearDirty());
	}

	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		// Default implementation, assume the holder is a player and that we need only to sync with that player
		return this.holder == player;
	}

	protected void trySync(ComponentKey<? extends RpgDataContainer<T, E>> key) {
		if (this.isDirty()) {
			if (!removed.isEmpty()) {
				key.sync(this.holder, (buf, p) -> writeSyncPacket(buf, this.underlying.values(), true));
				removed.clear();
			} else {
				key.sync(this.holder, (buf, p) -> writeSyncPacket(buf, gatherDirtyEntries(), false));
			}
			this.clearDirty();
		}
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
		writeSyncPacket(buf, this.underlying.values(), true);
	}

	private void writeSyncPacket(PacketByteBuf buf, Collection<E> entries, boolean clear) {
		buf.writeBoolean(clear);
		buf.writeInt(entries.size());
		for (E entry : entries) {
			buf.writeIdentifier(entry.getId());
			entry.writeToPacket(buf);
		}
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

	private List<E> gatherDirtyEntries() {
		List<E> entries = new ArrayList<>();

		for (E entry : this) {
			if (entry.isDirty()) entries.add(entry);
		}

		return entries;
	}
}
