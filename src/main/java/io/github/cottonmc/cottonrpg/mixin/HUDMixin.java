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
	private static final Identifier CRPG_BAR_TEX = new Identifier("cottonrpg", "textures/gui/rpg_bars.png");
	private static final int CRPG_FULL_BAR_WIDTH = 62;
	private static final float CRPG_DELTA_PER_TICK = 1f;
	private float crpg_tick_timer = 0;
	
	@Shadow
	private MinecraftClient client;

	

	@Inject(at = @At("RETURN"), method = "renderHotbar(F)V")
	void render(float f, CallbackInfo ci) {
		if (client.options.hudHidden) return;
		GlStateManager.enableBlend();
		GlStateManager.enableAlphaTest();
		
		crpg_tick_timer += f;
		int ticks = (int)(crpg_tick_timer/CRPG_DELTA_PER_TICK);
		crpg_tick_timer -= ticks*CRPG_DELTA_PER_TICK;
		
		final int[] height = new int[1];
		height[0] = CottonRPG.config.barsY;
		CharacterData data =  CharacterData.get(client.player);

		data.getResources().forEach((id, entry) -> {
			CharacterResource resource = CottonRPG.RESOURCES.get(id);
			for(int i = 0; i<ticks; i++) entry.clientTick();
			
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
			client.getTextureManager().bindTexture(CRPG_BAR_TEX);
			
			long rows = 1;
			
			if (!CottonRPG.config.bigResourceBars) {
				
				
				int boxes = (int)(entry.getMax() / resource.getUnitsPerBar()) - 1;
				rows += (Math.min(boxes, 35) / 12);
				
				
				
				double toDistribute = entry.getCurrentForRender();
				long fullBoxes = (long)toDistribute / resource.getUnitsPerBar();
				if (fullBoxes>boxes) fullBoxes = boxes;
				
				long barMaxAmount = resource.getUnitsPerBar();
				if (fullBoxes==boxes) {
					barMaxAmount = entry.getMax() - (long)(fullBoxes*resource.getUnitsPerBar());
					if (barMaxAmount==0) {
						fullBoxes--;
						barMaxAmount = resource.getUnitsPerBar(); //One full bar retrieved back from boxes
					}
					if (barMaxAmount>resource.getUnitsPerBar()) barMaxAmount -=resource.getUnitsPerBar();
				}
				
				toDistribute -= fullBoxes*resource.getUnitsPerBar(); //toDistribute is now the amount of the resource in the bar. all we need is the max now
				if (toDistribute>barMaxAmount) toDistribute -= resource.getUnitsPerBar();
				
				boolean needsPlus = boxes > 36;
				boolean plusOn = fullBoxes > 36;
				
				
				int barWidth = (int)((barMaxAmount / (float)resource.getUnitsPerBar()) * CRPG_FULL_BAR_WIDTH);
				if (barWidth<1) barWidth=1; //Never display a bar with length 0
				
				int fgWidth = (int)((toDistribute / (float)resource.getUnitsPerBar()) * CRPG_FULL_BAR_WIDTH);
				if (toDistribute>0 && fgWidth<=0) fgWidth=1; //never display an empty bar for *some* health
				
				//System.out.println("bar: "+toDistribute+"/"+barMaxAmount+" boxes: "+fullBoxes+"/"+boxes);
				
				//bar BG: left edge, middle, right edge
				
				//      x                    y    w         h    u  v
				guiRect(left,                top, 1,        5);
				guiRect(left + 1,            top, barWidth, 5,   1, 0);
				guiRect(left + barWidth + 1, top, 1,        5,  63, 0);
				
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
						guiRect(left, newTop, 6, 5, 0, 5);
						//blit(left, newTop, 6, 5, texUV(0), texUV(5), texUV(6), texUV(10));
						int newLeft = left + 5;
						//the rest of the boxes
						for (int j = 1; j < toDraw; j++) {
							guiRect(newLeft, newTop, 6, 5, 6, 5);
							//blit(newLeft, newTop, 6, 5, texUV(6), texUV(5), texUV(12), texUV(10));
							newLeft += 5;
						}
						if (needsPlus) {
							if (i < 2) {
								guiRect(newLeft, newTop, 3, 5, 19, 5);
								//blit(newLeft, newTop, 3, 5, texUV(19), texUV(5), texUV(22), texUV(10));
							} else {
								guiRect(newLeft, newTop, 5, 5, 22, 5);
								//blit(newLeft, newTop, 5, 5, texUV(22), texUV(5), texUV(27), texUV(10));
							}
						}
						newTop += 4;
					}
				}

				GlStateManager.color4f(r, g, b, 1.0f);
				//bar FG: left edge, middle, right edge
				guiRect(left, top, 1, 5, 0, 10);
				guiRect(left + 1, top, fgWidth, 5, 1, 10);
				guiRect(left + fgWidth + 1, top, 1, 5, 63, 10);
				//blit(left, top, 1, 5, texUV(0), texUV(10), texUV(1), texUV(15));
				//blit(left + 1, top, fgWidth, 5, texUV(1), texUV(10), texUV(fgWidth + 1), texUV(15));
				//blit(left + fgWidth + 1, top, 1, 5, texUV(63), texUV(10), texUV(64), texUV(15));
				if (fullBoxes > 0) {
					int boxesLeft = (int)fullBoxes;
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

	private static final double PX = 1/256d;

	/** Draws a rectangle assuming a 256x texture bound, at one texel per gui pixel */
	private static void guiRect(int x, int y, int width, int height) {
		innerBlit(x, y, x+width, y+height, 0, 0, 0, width*PX, height*PX);
	}

	/** Draws a rectangle assuming a 256x texture bound, at one texel per gui pixel and at the given texture offset */
	private static void guiRect(int x, int y, int width, int height, int texX, int texY) {
		double u1 = texX*PX;
		double v1 = texY*PX;
		innerBlit(x, y, x+width, y+height, 0, u1, v1, u1+(width*PX), v1+(height*PX));
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
