package io.github.cottonmc.cottonrpg.data;

import com.google.common.collect.Iterators;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Consumer;

public abstract class ProxyRpgDataContainer<T extends RpgDataType, E extends RpgDataEntry<T>> extends BaseRpgDataContainer<T, E> {
	private final RpgDataContainer<T, E> parent;
	private final RpgDataContainer<T, E> child;

	public ProxyRpgDataContainer(RpgDataContainer<T, E> parent, @Nullable RpgDataContainer<T, E> child) {
		super(null);
		this.parent = parent;
		this.child = child;
	}

	@Override
	public int size() {
		return parent.size() + (child != null ? child.size() : 0);
	}

	@Override
	public boolean has(T type) {
		return super.has(type);
	}

	@Override
	public E get(T type) {
		return super.get(type);
	}

	@Override
	public @NotNull Iterator<E> iterator() {
		if (child != null) return Iterators.concat(child.iterator(), parent.iterator());
		return parent.iterator();
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		if (child != null) child.forEach(action);
		parent.forEach(action);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Can't clear data from the proxy! Get the parent or child instead!");
	}

	@Override
	public E remove(T type) {
		throw new UnsupportedOperationException("Can't remove data from the proxy! Get the parent or child instead!");
	}

	@Override
	public E giveIfAbsent(Identifier id) {
		throw new UnsupportedOperationException("Can't add data from the proxy! Get the parent or child instead!");
	}

	@Override
	public E giveIfAbsent(T type) {
		throw new UnsupportedOperationException("Can't add data from the proxy! Get the parent or child instead!");
	}

	@Override
	public boolean isDirty() {
		throw new UnsupportedOperationException("The proxy can never be dirty! Get the parent or child instead!");
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
		throw new UnsupportedOperationException("Can't sync a proxy! Get the parent or child instead!");
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		throw new UnsupportedOperationException("Can't sync a proxy! Get the parent or child instead!");
	}

	@Override
	public void serverTick() {
		// NO-OP
	}

	public RpgDataContainer<T, E> getParent() {
		return parent;
	}

	public RpgDataContainer<T, E> getChild() {
		return child;
	}
}
