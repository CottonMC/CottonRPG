package io.github.cottonmc.cottonrpg.prereq;

import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClass;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResource;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A simple interface which allows checking various player-related stuff.
 * Should be adapted to work with items later.
 */
public interface Prerequisite extends Predicate<PlayerEntity> {
	static Prerequisite none() {
		return True.ALWAYS_TRUE;
	}

	static Prerequisite any(Prerequisite... prerequisites) {
		return new Any(prerequisites);
	}

	static Prerequisite all(Prerequisite... prerequisites) {
		return new All(prerequisites);
	}

	static Prerequisite not(Prerequisite prerequisite) {
		return new Not(prerequisite);
	}

	static Prerequisite hasClass(CharacterClass charClass, int level) {
		return new WantsClass(new Lazy<>(() -> charClass), level);
	}

	static Prerequisite hasClass(Identifier charClass, int level) {
		return new WantsClass(new Lazy<>(() -> CottonRPG.CLASSES.get(charClass)), level);
	}

	static Prerequisite hasResource(CharacterResource charResource, int amount) {
		return new WantsResource(new Lazy<>(() -> charResource), amount);
	}

	static Prerequisite hasResource(Identifier charResource, int amount) {
		return new WantsResource(new Lazy<>(() -> CottonRPG.RESOURCES.get(charResource)), amount);
	}

	/**
	 * @return The text component for this prerequisite.
	 */
	Text getDescription();

	/**
	 * @return All the children of this prerequisite
	 */
	Prerequisite[] getChildren();

	/**
	 * @return The text components of this prerequisite and its children.
	 */
	default List<Text> describe() {
		List<Text> text = new ArrayList<>();
		text.add(getDescription());
		Prerequisite[] children = getChildren();
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				List<Text> lines = children[i].describe();
				for (Text line : lines) {
					text.add(new LiteralText("  ").append(line));
				}
			}
		}
		return text;
	}

	/**
	 * Prerequisite that always returns true.
	 */
	class True implements Prerequisite {
		public static final True ALWAYS_TRUE = new True();

		@Override
		public boolean test(PlayerEntity player) {
			return true;
		}

		@Override
		public Text getDescription() {
			return new TranslatableText("prereq.cottonrpg.true");
		}

		@Override
		public Prerequisite[] getChildren() {
			return new Prerequisite[0];
		}
	}

	/**
	 * Prerequisite that requires all children to be true.
	 */
	class All implements Prerequisite {
		private final Prerequisite[] prereqs;

		public All(Prerequisite... ps) {
			prereqs = ps;
		}

		@Override
		public boolean test(PlayerEntity player) {
			for (int i = 0; i < prereqs.length; i++) {
				if (!prereqs[i].test(player)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public Prerequisite[] getChildren() {
			return prereqs;
		}

		@Override
		public Text getDescription() {
			return new TranslatableText("prereq.cottonrpg.all");
		}
	}

	/**
	 * Prerequisite that requires at least one child to be true.
	 */
	class Any implements Prerequisite {
		private final Prerequisite[] prereqs;

		public Any(Prerequisite... prereqs) {
			this.prereqs = prereqs;
		}

		@Override
		public boolean test(PlayerEntity player) {
			for (Prerequisite prereq : prereqs) {
				if (prereq.test(player)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Prerequisite[] getChildren() {
			return prereqs;
		}

		@Override
		public Text getDescription() {
			return new TranslatableText("prereq.cottonrpg.any");
		}
	}

	/**
	 * Prerequisite that requires that is child is false.
	 */
	class Not implements Prerequisite {
		private final Prerequisite prereq;

		public Not(Prerequisite prereq) {
			this.prereq = prereq;
		}

		@Override
		public boolean test(PlayerEntity player) {
			return !prereq.test(player);
		}

		@Override
		public Prerequisite[] getChildren() {
			return new Prerequisite[]{prereq};
		}

		@Override
		public Text getDescription() {
			return new TranslatableText("prereq.cottonrpg.not");
		}
	}

	/**
	 * Prerequisite that requires the player to have a certain level of a certain class.
	 */
	class WantsClass implements Prerequisite {
		private final Lazy<CharacterClass> classId;
		private final int level;

		public WantsClass(Lazy<CharacterClass> classId, int level) {
			this.classId = classId;
			this.level = level;
		}

		@Override
		public boolean test(PlayerEntity player) {
			CharacterClasses classes = CharacterData.get(player).getClasses();
			if (!classes.has(classId.get())) return false;
			return classes.get(classId.get()).getLevel() >= level;
		}

		@Override
		public Prerequisite[] getChildren() {
			return null;
		}

		@Override
		public Text getDescription() {
			return new TranslatableText(
				"prereq.cottonrpg.wants_class",
				classId.get().getName(),
				level
			);
		}
	}

	/**
	 * Prerequisite that requires the player to have a certain amount of a resource.
	 */
	class WantsResource implements Prerequisite {
		private final Lazy<CharacterResource> resourceId;
		private final int amount;

		public WantsResource(Lazy<CharacterResource> resid, int amt) {
			this.resourceId = resid;
			this.amount = amt;
		}

		@Override
		public boolean test(PlayerEntity player) {
			CharacterResources resources = CharacterData.get(player).getResources();
			if (!resources.has(resourceId.get())) return false;
			return resources.get(resourceId.get()).getCurrent() >= amount;
		}

		@Override
		public Prerequisite[] getChildren() {
			return null;
		}

		@Override
		public Text getDescription() {
			return new TranslatableText(
				"prereq.cottonrpg.wants_resource",
				resourceId.get().getName(),
				amount
			);
		}
	}
}
