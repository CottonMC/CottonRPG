package io.github.cottonmc.cottonrpg.data;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCharacterData extends CharacterData implements ServerTickingComponent {
    private final PlayerEntity player;

    public PlayerCharacterData(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {
        getResources().sync((ServerPlayerEntity) this.player);
        getSkills().sync((ServerPlayerEntity) this.player);
        getResources().forEach((id, resource) -> resource.tick());
        getSkills().forEach((id, skill) -> skill.tick());
    }
}
