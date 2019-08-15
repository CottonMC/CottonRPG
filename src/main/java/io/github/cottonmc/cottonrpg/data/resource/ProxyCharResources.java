package io.github.cottonmc.cottonrpg.data.resource;

import com.google.common.collect.ImmutableMap;
import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ProxyCharResources extends CharacterResources {
	private CharacterResources parent;
	private CharacterResources child;

	public ProxyCharResources(CharacterResources parent, CharacterResources child) {
		this.parent = parent;
		this.child = child;
	}

	@Override
	public int getSize() {
		return parent.getSize() + child.getSize();
	}

	@Override
	public boolean has(CharacterResource resource) {
		return has(CottonRPG.RESOURCES.getId(resource));
	}

	@Override
	public boolean has(Identifier id) {
		if (child.has(id)) return true;
		else return parent.has(id);
	}

	@Override
	public CharacterResourceEntry get(CharacterResource resource) {
		return get(CottonRPG.RESOURCES.getId(resource));
	}

	@Override
	public CharacterResourceEntry get(Identifier id) {
		if (child.has(id)) return child.get(id);
		else return parent.get(id);
	}

	@Override
	public void forEach(BiConsumer<Identifier, CharacterResourceEntry> consumer) {
		child.forEach(consumer);
		parent.forEach(consumer);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Can't clear resource from the proxy! Get the parent or child instead!");
	}

	@Override
	public CharacterResourceEntry remove(CharacterResource resource) {
		throw new UnsupportedOperationException("Can't remove a resource from the proxy! Get the parent or child instead!");
	}

	@Override
	public CharacterResourceEntry remove(Identifier id) {
		throw new UnsupportedOperationException("Can't remove a resource from the proxy! Get the parent or child instead!");
	}

	@Override
	public void giveIfAbsent(CharacterResourceEntry resource) {
		throw new UnsupportedOperationException("Can't add a resource from the proxy! Get the parent or child instead!");
	}

	@Override
	public boolean isDirty() {
		throw new UnsupportedOperationException("The proxy can never be dirty! Get the parent or child instead!");
	}

	@Override
	public void sync(ServerPlayerEntity player) {
		throw new UnsupportedOperationException("Can't sync a proxy! Get the parent or child instead!");
	}

	@Override
	public Map<Identifier, CharacterResourceEntry> getAll() {
		HashMap<Identifier, CharacterResourceEntry> ret = new HashMap<>(parent.getAll());
		//overwrites parent's, so still has higher priority
		ret.putAll(child.getAll());
		return ImmutableMap.copyOf(ret);
	}

	public CharacterResources getParent() {
		return parent;
	}

	public CharacterResources getChild() {
		return child;
	}
}
