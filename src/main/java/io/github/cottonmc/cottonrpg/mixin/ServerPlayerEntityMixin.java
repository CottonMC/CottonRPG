package io.github.cottonmc.cottonrpg.mixin;

import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.util.CharacterDataHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

	@Inject(method = "copyFrom", at = @At("HEAD"))
	private void copyPlayerData(ServerPlayerEntity player, boolean keepAll, CallbackInfo ci) {
		((CharacterDataHolder)this).crpg_setClasses(CharacterData.get(player).getClasses());
		((CharacterDataHolder)this).crpg_setResources(CharacterData.get(player).getResources());
		((CharacterDataHolder)this).crpg_setSkills(CharacterData.get(player).getSkills());
	}
}
