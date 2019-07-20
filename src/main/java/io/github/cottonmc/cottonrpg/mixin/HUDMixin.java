package io.github.cottonmc.cottonrpg.mixin;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.cottonmc.cottonrpg.ResourceBarRegistry;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponentType;
import io.github.cottonmc.cottonrpg.util.RPGPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class HUDMixin {

  @Shadow
  private int scaledWidth;
  @Shadow
  private int scaledHeight;
  
  @Shadow @Mutable
  private MinecraftClient client;
  
  @Shadow
  private TextRenderer getFontRenderer() { return null; }
  
  
  @Inject(at = @At("RETURN"), method = "renderHotbar(F)V")
  void render(float f, CallbackInfo ci) {
    if (client.options.hudHidden) return;
    GlStateManager.enableBlend();
    GlStateManager.enableAlphaTest();

    TextRenderer t = getFontRenderer();
    
    AtomicInteger i = new AtomicInteger(1);
    RPGPlayer p = (RPGPlayer) client.player;
    
    p.forEachRPGResourceBar((id, cons) -> {
      ResourceBarComponentType comp = ResourceBarRegistry.INSTANCE.get(id);
      if (comp.getVisibility() != ResourceBarComponentType.ResourceVisibility.HUD)
        return;
      Color color = comp.getColor();
      
      // Coords
      float left = 16.0f;
      float right = left + 128.0f;
      float top = 16.0f+32.0f*(i.get()-1);
      float bartop = top+16.0f;
      float bottom = top+32.0f;
      
      // Pane
      GlStateManager.color4f(0.0f, 0.0f, 0.0f, 0.8f);
      GlStateManager.begin(GL11.GL_QUADS);
        GlStateManager.vertex3f(left, top, 1.0f);
        GlStateManager.vertex3f(left, bottom, 1.0f);
        GlStateManager.vertex3f(right, bottom, 1.0f);
        GlStateManager.vertex3f(right, top, 1.0f);
      GlStateManager.end();
      
      // Text
      /*
      if (((color.getRed() + color.getGreen() + color.getBlue()) / 3) > 128) {
        GlStateManager.color4f(0.3f, 0.3f, 0.3f, 1.0f);
      } else {
        GlStateManager.color4f(0.9f, 0.9f, 0.9f, 1.0f);
      }
      */
      GlStateManager.color4f(0.9f, 0.9f, 0.9f, 1.0f);
      t.drawWithShadow(comp.getName().asFormattedString(), left, top, 1);
      
      // Bar
      GlStateManager.color4f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 1.0f);
      GlStateManager.begin(GL11.GL_QUADS);
        GlStateManager.vertex3f(left, bartop, 1.0f);
        GlStateManager.vertex3f(left, bottom, 1.0f);
        GlStateManager.vertex3f(right, bottom, 1.0f);
        GlStateManager.vertex3f(right, bartop, 1.0f);
      GlStateManager.end();

      // Increment
      i.incrementAndGet();
    });
    
    GlStateManager.disableAlphaTest();
    GlStateManager.disableBlend();
  }
}
