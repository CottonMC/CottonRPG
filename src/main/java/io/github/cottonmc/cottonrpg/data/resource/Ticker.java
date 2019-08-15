package io.github.cottonmc.cottonrpg.data.resource;

import net.minecraft.nbt.CompoundTag;

public interface Ticker {
	/**
	 * Get a ticker that never ticks and never triggers sync.
	 */
	Ticker EMPTY = new EmptyTicker();

	/**
	 * Called every tick to process logic for affecting a resource over time.
	 * @param entry The entry of this resource.
	 */
	void tick(CharacterResourceEntry entry);

	/**
	 * @return A tag containing all the information this ticker needs to persist across loads.
	 */
	CompoundTag toTag();

	/**
	 * @param tag The tag saved by `toTag()`.
	 * @return A functioning state of this ticker as identical as possible to the one saved to the tag.
	 */
	Ticker fromTag(CompoundTag tag);

	/**
	 * Mark that this ticker has changed and needs to be saved/synced.
	 */
	void markDirty();

	/**
	 * @return Whether this ticker needs to be saved/synced.
	 */
	boolean isDirty();

	/**
	 * Clear the mark for this ticker being dirty.
	 */
	void clearDirty();

	class EmptyTicker implements Ticker {
		@Override
		public void tick(CharacterResourceEntry entry) { }

		@Override
		public CompoundTag toTag() { return new CompoundTag(); }

		@Override
		public Ticker fromTag(CompoundTag tag) { return this; }

		@Override
		public void markDirty() { }

		@Override
		public boolean isDirty() { return false; }

		@Override
		public void clearDirty() { }
	}
}
