package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CharacterSkills {
	private ArrayList<Identifier> removed = new ArrayList<>();

	private final Map<Identifier, CharacterSkillEntry> underlying = new HashMap<>();

	public int getSize() {
		synchronized(underlying) {
			return underlying.size();
		}
	}

	public void clear() {
		synchronized(underlying) {
			underlying.clear();
		}
	}

	public boolean has(CharacterSkill skill) {
		return has(CottonRPG.SKILLS.getId(skill));
	}

	public boolean has(Identifier id) {
		synchronized(underlying) {
			return underlying.containsKey(id);
		}
	}

	public CharacterSkillEntry get(CharacterSkill skill) {
		return get(CottonRPG.SKILLS.getId(skill));
	}

	public CharacterSkillEntry get(Identifier id) {
		synchronized(underlying) {
			return underlying.get(id);
		}
	}

	public void giveIfAbsent(CharacterSkillEntry skill) {
		synchronized(underlying) {
			underlying.putIfAbsent(skill.id, skill);
		}
		skill.markDirty();
	}

	public CharacterSkillEntry remove(CharacterSkill skill) {
		return remove(CottonRPG.SKILLS.getId(skill));
	}

	public CharacterSkillEntry remove(Identifier id) {
		CharacterSkillEntry entry = underlying.remove(id);
		if (entry!=null) removed.add(id);
		return entry;
	}

	public void forEach(BiConsumer<Identifier, CharacterSkillEntry> consumer) {
		synchronized(underlying) {
			underlying.forEach(consumer);
		}
	}

	public boolean isDirty() {
		if (!removed.isEmpty()) return true;

		for(CharacterSkillEntry entry : underlying.values()) {
			if (entry.isDirty()) return true;
		}

		return false;
	}

	public void sync(ServerPlayerEntity player) {
		if (!isDirty()) return;

		if (!removed.isEmpty()) {
			CottonRPGNetworking.batchSyncSkills(player, this, true);
			removed.clear();
		} else {
			CottonRPGNetworking.batchSyncSkills(player, this, false);
		}
	}
}
