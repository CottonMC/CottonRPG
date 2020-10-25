package io.github.cottonmc.cottonrpg.client;

import io.github.cottonmc.cottonrpg.data.CharacterData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CottonRPGClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new RpgHud());
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> CharacterData.get(minecraftClient.player).getResources().forEach((id, res) -> res.clientTick()));
    }
}
