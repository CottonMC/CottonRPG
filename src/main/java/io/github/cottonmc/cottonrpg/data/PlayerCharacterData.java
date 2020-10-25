package io.github.cottonmc.cottonrpg.data;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.cottonmc.cottonrpg.data.clazz.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResourceEntry;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkillEntry;
import io.github.cottonmc.cottonrpg.data.skill.CharacterSkills;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCharacterData extends CharacterData implements ServerTickingComponent, AutoSyncedComponent {
	private final PlayerEntity player;

	public PlayerCharacterData(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player == this.player;
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		buf.writeByte(CharacterClasses.SYNC_FLAG | CharacterResources.SYNC_FLAG | CharacterSkills.SYNC_FLAG);
		this.getClasses().writeSyncPacket(buf);
		this.getResources().writeSyncPacket(buf);
		this.getSkills().writeSyncPacket(buf);
	}

	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		int syncFlags = buf.readByte();
		if ((syncFlags & CharacterClasses.SYNC_FLAG) != 0) {
			this.getClasses().applySyncPacket(buf);
		}
		if ((syncFlags & CharacterResources.SYNC_FLAG) != 0) {
			this.getResources().applySyncPacket(buf);
		}
		if ((syncFlags & CharacterSkills.SYNC_FLAG) != 0) {
			this.getSkills().applySyncPacket(buf);
		}
	}

	private int getDirtyFlags() {
		int ret = 0;
		if (getClasses().isDirty()) ret |= CharacterClasses.SYNC_FLAG;
		if (getResources().isDirty()) ret |= CharacterResources.SYNC_FLAG;
		if (getSkills().isDirty()) ret |= CharacterSkills.SYNC_FLAG;
		return ret;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void serverTick() {
		int syncFlags = getDirtyFlags();
		if (syncFlags != 0) {
			KEY.sync(this.player, (buf, recipient) -> {
				buf.writeByte(syncFlags);
				if ((syncFlags & CharacterClasses.SYNC_FLAG) != 0) {
					getClasses().writeSyncPacket(buf);
				}
				if ((syncFlags & CharacterResources.SYNC_FLAG) != 0) {
					getResources().writeSyncPacket(buf);
				}
				if ((syncFlags & CharacterSkills.SYNC_FLAG) != 0) {
					getSkills().writeSyncPacket(buf);
				}
			});
		}
		getResources().forEach(CharacterResourceEntry::tick);
		getSkills().forEach(CharacterSkillEntry::tick);
	}
}
