package io.github.cottonmc.cottonrpg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class HUDMixin {

  @Inject(at = @At("TAIL"), method = "render(F)V")
  void render(float f, CallbackInfo ci) {
    
  }
}
