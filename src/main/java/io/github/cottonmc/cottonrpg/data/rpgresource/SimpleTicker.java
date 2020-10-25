package io.github.cottonmc.cottonrpg.data.rpgresource;

import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleTicker implements Ticker {

	private long ticker = 0;
	private long tickTo;
	private Consumer<CharacterResourceEntry> handler;
	private Predicate<CharacterResourceEntry> shouldTick;
	private transient boolean dirty = false;

	public SimpleTicker(long tickTo, Consumer<CharacterResourceEntry> handler, Predicate<CharacterResourceEntry> shouldTick) {
		this.tickTo = tickTo;
		this.handler = handler;
		this.shouldTick = shouldTick;
	}

	@Override
	public void tick(CharacterResourceEntry entry) {
		if (shouldTick.test(entry)) {
			if (ticker++ >= tickTo) {
				ticker = 0;
				handler.accept(entry);
			}
			markDirty();
		}
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putLong("Ticks", ticker);
		return tag;
	}

	@Override
	public SimpleTicker fromTag(CompoundTag tag) {
		ticker = tag.getLong("Ticks");
		return this;
	}

	@Override
	public void markDirty() {
		dirty = true;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void clearDirty() {
		dirty = false;
	}

}
