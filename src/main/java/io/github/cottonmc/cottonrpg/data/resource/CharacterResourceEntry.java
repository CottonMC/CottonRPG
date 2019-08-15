package io.github.cottonmc.cottonrpg.data.resource;

import io.github.cottonmc.cottonrpg.CottonRPG;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class CharacterResourceEntry {
	private final double SCRAMBLE_CAP = 200d;
	private final double SCRAMBLE_FLOOR = 0.01d;
	
	public final Identifier id;
	private CharacterResource res;
	private long current;
	private long max;
	private Ticker ticker;
	private transient boolean dirty = false;
	private transient double scramble;

	public CharacterResourceEntry(Identifier id) {
		this.id = id;
		this.res = CottonRPG.RESOURCES.get(id);
		current = res.getDefaultLevel();
		max = res.getDefaultMaxLevel();
		this.ticker = res.makeTicker(this);
	}

	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putLong("CurrentLevel", current);
		tag.putLong("MaxLevel", max);
		tag.put("Ticker", ticker.toTag());
		return tag;
	}

	public CharacterResourceEntry fromTag(CompoundTag tag) {
		this.current = tag.getLong("CurrentLevel");
		this.max = tag.getLong("MaxLevel");
		this.ticker = res.makeTicker(this).fromTag(tag.getCompound("Ticker"));
		markDirty();
		return this;
	}

	public double getCurrentForRender() {
		return scramble;
	}

	public void setRenderValue(double scramble) {
		this.scramble = scramble;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long l) {
		markDirty();
		this.current = Math.min(l, getMax());
	}

	public long getMax() {
		return max;
	}

	public void setMax(long l) {
		markDirty();
		this.max = l;
	}

	public void tick() {
		ticker.tick(this);
		if (ticker.isDirty()) {
			markDirty();
			ticker.clearDirty();
		}
	}
	
	public void clientTick() {
		if (scramble>max) scramble=max; //Don't spend ages coming back from overfull
		if (scramble!=current) {
			double scrambleRate = SCRAMBLE_CAP/max;
			double delta = (current-scramble)/scrambleRate;
			if (delta<0 && delta>-SCRAMBLE_FLOOR) delta = -SCRAMBLE_FLOOR;
			else if (delta>0 && delta<SCRAMBLE_FLOOR) delta = SCRAMBLE_FLOOR;
			//now scramble is at least SCRAMBLE_FLOOR and up to max/SCRAMBLE_CAP of the distance we need to cover
			if (delta<0) {
				if (scramble+delta<current) {
					scramble = current;
				} else {
					scramble+=delta;
				}
			} else {
				if (scramble+delta>current) {
					scramble = current;
				} else {
					scramble += delta;
				}
			}
		}
	}

	public void markDirty() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clearDirty() {
		dirty = false;
	}
}