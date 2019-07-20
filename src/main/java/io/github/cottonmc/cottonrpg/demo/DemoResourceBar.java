package io.github.cottonmc.cottonrpg.demo;

import java.awt.Color;

import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DemoResourceBar extends ResourceBarComponent {

  public static final Identifier RESOURCE_ID = new Identifier("cottonrpg", "demo_resource");
  
  private int ticker = 0;
  
  public DemoResourceBar(PlayerEntity owner) {
    super(owner);
  }

  @Override
  public void tick() {
    if (ticker++ >= 60) {
      ticker = 0;
      setValue(value + 10);
    }
  }

  @Override
  public Identifier getID() {
    return RESOURCE_ID;
  }

  @Override
  public Color getColor() {
    return Color.GREEN;
  }
  
  /*
  @Override
  public ComponentType<?> getComponentType() {
    return null;
  }
  */

  @Override
  protected long getDefaultMax() {
    return 128;
  }

  @Override
  protected long getDefaultValue() {
    return 0;
  }
  
  @Override
  public ResourceVisibility getVisibility() {
    return ResourceVisibility.HUD;
  }

}
