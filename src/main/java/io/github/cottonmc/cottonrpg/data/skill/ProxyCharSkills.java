package io.github.cottonmc.cottonrpg.data.skill;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class ProxyCharSkills extends CharacterSkills {
	CharacterSkills parent;
	CharacterSkills child;

	public ProxyCharSkills(CharacterSkills parent, @Nullable CharacterSkills child) {
		this.parent = parent;
		this.child = child;
	}

	@Override
	public int getSize() {
		return parent.getSize() + (child != null? child.getSize() : 0);
	}

	@Override
	public boolean has(CharacterSkill skill) {
		return has(CottonRPG.SKILLS.getId(skill));
	}

	@Override
	public boolean has(Identifier id) {
		if (child != null && child.has(id)) return true;
		else return parent.has(id);
	}

	@Override
	public CharacterSkillEntry get(CharacterSkill skill) {
		return get(CottonRPG.SKILLS.getId(skill));
	}

	@Override
	public CharacterSkillEntry get(Identifier id) {
		if (child != null && child.has(id)) return child.get(id);
		else return parent.get(id);
	}

	@Override
	public void forEach(BiConsumer<Identifier, CharacterSkillEntry> consumer) {
		if (child != null) child.forEach(consumer);
		parent.forEach(consumer);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Can't clear skills from the proxy! Get the parent or child instead!");
	}

	@Override
	public CharacterSkillEntry remove(CharacterSkill skill) {
		throw new UnsupportedOperationException("Can't remove a class from the proxy! Get the parent or child instead!");
	}

	@Override
	public CharacterSkillEntry remove(Identifier id) {
		throw new UnsupportedOperationException("Can't remove a class from the proxy! Get the parent or child instead!");
	}

	@Override
	public void giveIfAbsent(CharacterSkillEntry skill) {
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

	public CharacterSkills getParent() {
		return parent;
	}

	public CharacterSkills getChild() {
		return child;
	}
}
