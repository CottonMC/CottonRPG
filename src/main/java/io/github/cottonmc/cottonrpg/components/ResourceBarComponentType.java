package io.github.cottonmc.cottonrpg.components;

import java.awt.Color;

import io.github.cottonmc.cottonrpg.util.Ticker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public abstract class ResourceBarComponentType {

  public enum ResourceVisibility {
    INVISIBLE,
    MENU,
    HUD
  }
  
  public abstract ResourceBarComponent construct(PlayerEntity player);
  public abstract Identifier getID();
  
  public abstract Ticker makeTicker(ResourceBarComponent rb);

  public abstract Color getColor();
  public abstract ResourceVisibility getVisibility();
  
  protected long getDefaultMax() {
    return 0;
  }
  protected long getDefaultValue() {
    return 16;
  }
  
}
