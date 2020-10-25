package io.github.cottonmc.cottonrpg.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cottonrpg.CottonRPG;
import io.github.cottonmc.cottonrpg.data.CharacterData;
import io.github.cottonmc.cottonrpg.data.resource.CharacterResource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public final class RpgHud extends DrawableHelper implements HudRenderCallback {
	private static final Identifier CRPG_BAR_TEX = new Identifier("cottonrpg", "textures/gui/rpg_bars.png");
	private static final int CRPG_FULL_BAR_WIDTH = 62;
	private final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onHudRender(MatrixStack matrices, float tickDelta) {
		if (client.options.hudHidden) return;
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();

		final int[] height = new int[1];
		height[0] = CottonRPG.config.barsY;
		CharacterData data =  CharacterData.get(client.player);

		data.getResources().forEach((res, entry) -> {
			CharacterResource resource = entry.getType();
			Identifier id = resource.getId();
			
			if (resource.getVisibility() != CharacterResource.ResourceVisibility.HUD) return;
			int color = resource.getColor();

			//coords for this bar
			int left = CottonRPG.config.barsX;
			int top = height[0];

			//draw icon
			Identifier icon = new Identifier(id.getNamespace(), "textures/rpg_resource/" + id.getPath() + ".png");
			client.getTextureManager().bindTexture(icon);
			drawTexture(matrices, left, top, 9, 9, 0, 0, 1, 1, 1, 1);

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
					barMaxAmount = entry.getMax() - (fullBoxes*resource.getUnitsPerBar());
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
				drawTexture(matrices, left, top, 0, 0, 1, 5);
				drawTexture(matrices, left + 1, top, 1, 0, barWidth, 5);
				drawTexture(matrices, left + barWidth + 1, top, 63, 0, 1, 5);

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
						drawTexture(matrices, left, newTop, 0, 5, 6, 5);
						//blit(left, newTop, 6, 5, texUV(0), texUV(5), texUV(6), texUV(10));
						int newLeft = left + 5;
						//the rest of the boxes
						for (int j = 1; j < toDraw; j++) {
							drawTexture(matrices, newLeft, newTop, 6, 5, 6, 5);
							//blit(newLeft, newTop, 6, 5, texUV(6), texUV(5), texUV(12), texUV(10));
							newLeft += 5;
						}
						if (needsPlus) {
							if (i < 2) {
								drawTexture(matrices, newLeft, newTop, 19, 5, 3, 5);
								//blit(newLeft, newTop, 3, 5, texUV(19), texUV(5), texUV(22), texUV(10));
							} else {
								drawTexture(matrices, newLeft, newTop, 22, 5, 5, 5);
								//blit(newLeft, newTop, 5, 5, texUV(22), texUV(5), texUV(27), texUV(10));
							}
						}
						newTop += 4;
					}
				}

				if (!CottonRPG.config.disableResourceColors) RenderSystem.color4f(r, g, b, 1.0f);
				//bar FG: left edge, middle, right edge
				drawTexture(matrices, left, top, 0, 10, 1, 5);
				drawTexture(matrices, left + 1, top, 1, 10, fgWidth, 5);
				drawTexture(matrices, left + fgWidth + 1, top, 63, 10, 1, 5);
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
						drawTexture(matrices, left, newTop, 0, 15, 6, 5);
						int newLeft = left + 5;
						//the rest of the boxes
						for (int j = 1; j < toDraw; j++) {
							drawTexture(matrices, newLeft, newTop, 6, 15, 6, 5);
							newLeft += 5;
						}
						if (plusOn) {
							if (i < 2) {
								drawTexture(matrices, newLeft, newTop, 19, 15, 3, 5);
							} else {
								drawTexture(matrices, newLeft, newTop, 22, 15, 5, 5);
							}
						}
						newTop += 4;
					}
				}
			} else {
				//bar BG: left edge, middle, right edge
				drawTexture(matrices, left, top, 0, 20, 1, 9);
				drawTexture(matrices, left + 1, top, 1, 20, CRPG_FULL_BAR_WIDTH, 9);
				drawTexture(matrices, left + 63, top, 63, 20, 1, 9);

				double toRender = entry.getCurrentForRender();

				RenderSystem.color4f(r, g, b, 1.0f);
				int fgWidth = (int)((toRender / (float)entry.getMax()) * CRPG_FULL_BAR_WIDTH);
				if (toRender>0 && fgWidth<=0) fgWidth=1; //never display an empty bar for *some* health
				//bar FG: left edge, middle, right edge
				drawTexture(matrices, left, top, 0, 29, 1, 9);
				drawTexture(matrices, left + 1, top, 1, 29, fgWidth, 9);
				drawTexture(matrices, left + fgWidth + 1, top, 63, 29, 1, 9);
			}

			// Increment
			height[0] += (12 + (4 * (rows - 1)));
			RenderSystem.color4f(1f, 1f, 1f, 1f);
		});

		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

}
