package io.github.cottonmc.cottonrpg.data;

import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.rpgskill.CharacterSkills;

public class ProxyCharacterData extends CharacterData {
	private final CharacterData parent;
	private final CharacterData child;

	private CharacterClasses.Proxy proxyClasses;
	private CharacterResources.Proxy proxyResources;
	private CharacterSkills.Proxy proxySkills;

	/**
	 * Create a proxy for interacting with multiple sets of data at once.
	 *
	 * @param parent The parent of the data sets. Checked last. Usually a player.
	 * @param child  The child of the data sets. Checked first. Usually an item.
	 */
	public ProxyCharacterData(CharacterData parent, CharacterData child) {
		super();
		this.parent = parent;
		this.child = child;
	}

	/**
	 * @return The classes of the parent and child.
	 */
	@Override
	public CharacterClasses getClasses() {
		if (proxyClasses == null) {
			proxyClasses = new CharacterClasses.Proxy(parent.getClasses(), child.getClasses());
		}
		return proxyClasses;
	}

	/**
	 * @return The resources of the parent and child.
	 */
	@Override
	public CharacterResources getResources() {
		if (proxyResources == null) {
			proxyResources = new CharacterResources.Proxy(parent.getResources(), child.getResources());
		}
		return proxyResources;
	}

	/**
	 * @return The skills of the parent and child.
	 */
	@Override
	public CharacterSkills getSkills() {
		if (proxySkills == null) {
			proxySkills = new CharacterSkills.Proxy(parent.getSkills(), child.getSkills());
		}
		return proxySkills;
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
