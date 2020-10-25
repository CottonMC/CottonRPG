package io.github.cottonmc.cottonrpg.data.clazz;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ProxyCharClasses extends CharacterClasses {
	private CharacterClasses parent;
	private CharacterClasses child;

	public ProxyCharClasses(CharacterClasses parent, CharacterClasses child) {
		this.parent = parent;
		this.child = child;
	}

	@Override
	public int getSize() {
		return parent.getSize() + child.getSize();
	}

	@Override
	public boolean has(CharacterClass clazz) {
		return has(CottonRPG.CLASSES.getId(clazz));
	}

	@Override
	public boolean has(Identifier id) {
		if (child.has(id)) return true;
		else return parent.has(id);
	}

	@Override
	public CharacterClassEntry get(CharacterClass clazz) {
		return get(CottonRPG.CLASSES.getId(clazz));
	}

	@Override
	public CharacterClassEntry get(Identifier id) {
		if (child.has(id)) return child.get(id);
		else return parent.get(id);
	}

	@Override
	public void forEach(BiConsumer<Identifier, CharacterClassEntry> consumer) {
		child.forEach(consumer);
		parent.forEach(consumer);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Can't clear clazz from the proxy! Get the parent or child instead!");
	}

	@Override
	public CharacterClassEntry remove(CharacterClass clazz) {
		throw new UnsupportedOperationException("Can't remove a class from the proxy! Get the parent or child instead!");
	}

	@Override
	public CharacterClassEntry remove(Identifier id) {
		throw new UnsupportedOperationException("Can't remove a class from the proxy! Get the parent or child instead!");
	}

	@Override
	public void giveIfAbsent(CharacterClassEntry clazz) {
		throw new UnsupportedOperationException("Can't add a class from the proxy! Get the parent or child instead!");
	}

	@Override
	public boolean isDirty() {
		throw new UnsupportedOperationException("The proxy can never be dirty! Get the parent or child instead!");
	}

	@Override
	public void sync(ServerPlayerEntity player) {
		throw new UnsupportedOperationException("Can't sync a proxy! Get the parent or child instead!");
	}

	public CharacterClasses getParent() {
		return parent;
	}

	public CharacterClasses getChild() {
		return child;
	}
}
