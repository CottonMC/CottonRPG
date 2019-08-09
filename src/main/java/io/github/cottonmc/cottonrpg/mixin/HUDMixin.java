package io.github.cottonmc.cottonrpg.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.CharacterResource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HUDMixin {
  
  @Shadow
  private MinecraftClient client;

  private static final Identifier BAR_TEX = new Identifier("cottonrpg", "textures/gui/rpg_bars.png");
  
  @Inject(at = @At("RETURN"), method = "renderHotbar(F)V")
  void render(float f, CallbackInfo ci) {
    if (client.options.hudHidden) return;
    GlStateManager.enableBlend();
    GlStateManager.enableAlphaTest();

    final int[] height = new int[1];
    height[0] = CottonRPG.config.barsY;
    CharacterData data =  CharacterData.get(client.player);
    
    data.getResources().forEach((id, entry) -> {
      CharacterResource resource = CottonRPG.RESOURCES.get(id);
      if (resource.getVisibility() != CharacterResource.ResourceVisibility.HUD) return;
      int color = resource.getColor();
      
      //coords for this bar
      int left = CottonRPG.config.barsX;
      int top = height[0];

      //draw icon
      Identifier icon = new Identifier(id.getNamespace(), "textures/rpg_resource/" + id.getPath() + ".png");
      client.getTextureManager().bindTexture(icon);
      blit(left, top, 9, 9);

      left += 10;

      //draw bar
      float r = (color >> 16 & 255) / 255f;
      float g = (color >> 8 & 255) / 255f;
      float b = (color & 255) / 255f;
      client.getTextureManager().bindTexture(BAR_TEX);

      int boxes = (int)(entry.getMax() / resource.getUnitsPerBar()) - 1;
      boolean needsPlus = boxes > 36;
      long rows = (Math.min(boxes, 35) / 12) + 1;
      int fullBoxes = (int)(entry.getCurrent() / resource.getUnitsPerBar()) - 1;
      boolean plusOn = fullBoxes > 36;

      if (!CottonRPG.config.bigResourceBars) {
        long aboveLastBox = entry.getMax() % resource.getUnitsPerBar();
        if (aboveLastBox == 0) aboveLastBox = resource.getUnitsPerBar();
        long remainder = entry.getCurrent() % resource.getUnitsPerBar();
        if (remainder == 0) remainder = resource.getUnitsPerBar();
        int bgLength = (int) (((float) aboveLastBox / (float) resource.getUnitsPerBar()) * 62F);
        int fgLength = (int) (((float) remainder / (float) aboveLastBox) * bgLength);
        if (fgLength == 0) fgLength = 1;

        //bar BG: left edge, middle, right edge
        blit(left, top, 1, 5, texUV(0), texUV(0), texUV(1), texUV(5));
        blit(left + 1, top, bgLength, 5, texUV(1), texUV(0), texUV(bgLength + 1), texUV(5));
        blit(left + bgLength + 1, top, 1, 5, texUV(63), texUV(0), texUV(64), texUV(5));
        if (boxes > 0) {
          int boxesLeft = boxes;
          int newTop = top + 4;
          for (int i = 0; i < rows; i++) {
            int toDraw = 12;
            if (boxesLeft > 12) {
              boxesLeft -= 12;
            } else {
              toDraw = boxesLeft;
            }
            //first box
            blit(left, newTop, 6, 5, texUV(0), texUV(5), texUV(6), texUV(10));
            int newLeft = left + 5;
            //the rest of the boxes
            for (int j = 1; j < toDraw; j++) {
              blit(newLeft, newTop, 6, 5, texUV(6), texUV(5), texUV(12), texUV(10));
              newLeft += 5;
            }
            if (needsPlus) {
              if (i < 2) {
                blit(newLeft, newTop, 3, 5, texUV(19), texUV(5), texUV(22), texUV(10));
              } else {
                blit(newLeft, newTop, 5, 5, texUV(22), texUV(5), texUV(27), texUV(10));
              }
            }
            newTop += 4;
          }
        }

        GlStateManager.color4f(r, g, b, 1.0f);
        //bar FG: left edge, middle, right edge
        blit(left, top, 1, 5, texUV(0), texUV(10), texUV(1), texUV(15));
        blit(left + 1, top, fgLength, 5, texUV(1), texUV(10), texUV(fgLength + 1), texUV(15));
        blit(left + fgLength + 1, top, 1, 5, texUV(63), texUV(10), texUV(64), texUV(15));
        if (fullBoxes > 0) {
          int boxesLeft = fullBoxes;
          int newTop = top + 4;
          for (int i = 0; i < rows; i++) {
            int toDraw = 12;
            if (boxesLeft > 12) {
              boxesLeft -= 12;
            } else {
              toDraw = boxesLeft;
            }
            //first box
            blit(left, newTop, 6, 5, texUV(0), texUV(15), texUV(6), texUV(20));
            int newLeft = left + 5;
            //the rest of the boxes
            for (int j = 1; j < toDraw; j++) {
              blit(newLeft, newTop, 6, 5, texUV(6), texUV(15), texUV(12), texUV(20));
              newLeft += 5;
            }
            if (plusOn) {
              if (i < 2) {
                blit(newLeft, newTop, 3, 5, texUV(19), texUV(15), texUV(22), texUV(20));
              } else {
                blit(newLeft, newTop, 5, 5, texUV(22), texUV(15), texUV(27), texUV(20));
              }
            }
            newTop += 4;
          }
        }
      } else {
        //bar BG: left edge, middle, right edge
        blit(left, top, 1, 9, texUV(0), texUV(20), texUV(1), texUV(29));
        blit(left + 1, top, 62, 9, texUV(1), texUV(20), texUV(63), texUV(29));
        blit(left + 63, top, 1, 9, texUV(63), texUV(20), texUV(64), texUV(29));

        GlStateManager.color4f(r, g, b, 1.0f);
        int fgLength = (int)(((float)entry.getCurrent() / (float)entry.getMax()) * 62f);
        //bar FG: left edge, middle, right edge
        blit(left, top, 1, 9, texUV(0), texUV(29), texUV(1), texUV(38));
        blit(left + 1, top, fgLength, 9, texUV(1), texUV(29), texUV(fgLength + 1), texUV(38));
        blit(left + fgLength + 1, top, 1, 9, texUV(63), texUV(29), texUV(64), texUV(38));
      }

      // Increment
      height[0] += (12 + (4 * (rows - 1)));
      GlStateManager.color4f(1f, 1f, 1f, 1f);
    });
    
    GlStateManager.disableAlphaTest();
    GlStateManager.disableBlend();
  }

  private static void blit(int x, int y, int width, int height) {
    blit(x, y, width, height, 0d, 0d, 1d, 1d);
  }

  private static void blit(int x, int y, int width, int height, double u1, double v1, double u2, double v2) {
    innerBlit(x, y, x+width, y+height, 0d, u1, v1, u2, v2);
  }

  private static void innerBlit(double x1, double y1, double x2, double y2, double z, double u1, double v1, double u2, double v2) {
    Tessellator tess = Tessellator.getInstance();
    BufferBuilder buffer = tess.getBufferBuilder();
    buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV);
    buffer.vertex(x1, y2, z).texture(u1, v2).next();
    buffer.vertex(x2, y2, z).texture(u2, v2).next();
    buffer.vertex(x2, y1, z).texture(u2, v1).next();
    buffer.vertex(x1, y1, z).texture(u1, v1).next();
    tess.draw();
  }

  private static double texUV(int orig) {
    return ((double)orig) / 256d;
  }
}
