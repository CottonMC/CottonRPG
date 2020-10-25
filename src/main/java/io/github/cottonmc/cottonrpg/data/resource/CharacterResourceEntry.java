package io.github.cottonmc.cottonrpg.data.resource;

import io.github.cottonmc.cottonrpg.data.RpgDataEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class CharacterResourceEntry implements RpgDataEntry<CharacterResource> {
	private static final double SCRAMBLE_CAP = 200d;
	private static final double SCRAMBLE_FLOOR = 0.01d;

	private final Identifier id;
	private final CharacterResource res;
	private long current;
	private long max;
	private Ticker ticker;
	private transient boolean dirty = false;
	private transient double scramble;

	public CharacterResourceEntry(CharacterResource res) {
		this.id = res.getId();
		this.res = res;
		this.current = res.getDefaultLevel();
		this.max = res.getDefaultMaxLevel();
		this.ticker = res.makeTicker(this);
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public CharacterResource getType() {
		return res;
	}

	@Override
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putLong("CurrentLevel", current);
		tag.putLong("MaxLevel", max);
		tag.put("Ticker", ticker.toTag());
		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.current = tag.getLong("CurrentLevel");
		this.max = tag.getLong("MaxLevel");
		this.ticker = getType().makeTicker(this).fromTag(tag.getCompound("Ticker"));
		markDirty();
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
		if (scramble > max) scramble = max; //Don't spend ages coming back from overfull
		if (scramble != current) {
			double scrambleRate = SCRAMBLE_CAP / max;
			double delta = (current - scramble) / scrambleRate;
			if (delta < 0 && delta > -SCRAMBLE_FLOOR) delta = -SCRAMBLE_FLOOR;
			else if (delta > 0 && delta < SCRAMBLE_FLOOR) delta = SCRAMBLE_FLOOR;
			//now scramble is at least SCRAMBLE_FLOOR and up to max/SCRAMBLE_CAP of the distance we need to cover
			if (delta < 0) {
				if (scramble + delta < current) {
					scramble = current;
				} else {
					scramble += delta;
				}
			} else {
				if (scramble + delta > current) {
					scramble = current;
				} else {
					scramble += delta;
				}
			}
		}
	}

	@Override
	public void writeToPacket(PacketByteBuf buf) {
		buf.writeLong(this.getCurrent());
		buf.writeLong(this.getMax());
	}

	@Override
	public void readFromPacket(PacketByteBuf buf) {
		this.setCurrent(buf.readLong());
		this.setMax(buf.readLong());
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
