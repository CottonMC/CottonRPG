package io.github.cottonmc.cottonrpg.util.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class PlayerTarget implements Target<PlayerEntity> {
	private Collection<PlayerEntity> players;

	public PlayerTarget(Collection<PlayerEntity> players) {
		this.players = players;
	}

	@Override
	public Collection<PlayerEntity> allSubjects() {
		return players;
	}

	@Override
	public PlayerEntity closestSubject(BlockPos pos) {
		double min = -1;
		PlayerEntity minPlayer = null;
		for (PlayerEntity player : players) {
			double distance = player.getBlockPos().getSquaredDistance(pos);
			if (min == -1) {
				min = distance;
				minPlayer = player;
			}
			else if (distance < min) {
				min = distance;
				minPlayer = player;
			}
		}
		return minPlayer;
	}
}
