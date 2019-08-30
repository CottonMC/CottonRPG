package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.clazz.ProxyCharClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.resource.ProxyCharResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;
import io.github.cottonmc.cottonrpg.data.skill.ProxyCharSkills;

public class ProxyCharData extends CharacterData {
	private CharacterData parent;
	private CharacterData child;

	/**
	 * Create a proxy for interacting with multiple sets of data at once.
	 * @param parent The parent of the data sets. Checked last. Usually a player.
	 * @param child The child of the data sets. Checked first. Usually an item.
	 */
	public ProxyCharData(CharacterData parent, CharacterData child) {
		this.parent = parent;
		this.child = child;
	}

	/**
	 * @return The classes of the parent and child.
	 */
	@Override
	public CharacterClasses getClasses() {
		return new ProxyCharClasses(parent.getClasses(), child.getClasses());
	}

	/**
	 * @return The resources of the parent and child.
	 */
	@Override
	public CharacterResources getResources() {
		return new ProxyCharResources(parent.getResources(), child.getResources());
	}

	/**
	 * @return The skills of the parent and child.
	 */
	@Override
	public CharacterSkills getSkills() {
		return new ProxyCharSkills(parent.getSkills(), child.getSkills());
	}

	/**
	 * @return All the data of the parent.
	 */
	public CharacterData getParent() {
		return parent;
	}

	/**
	 * @return All the data of the child.
	 */
	public CharacterData getChild() {
		return child;
	}
}
