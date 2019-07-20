package io.github.cottonmc.cottonrpg.demo;

import java.awt.Color;

import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponentType;
import io.github.cottonmc.cottonrpg.components.SimpleResourceBarComponent;
import io.github.cottonmc.cottonrpg.util.Ticker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DemoResourceBar extends SimpleResourceBarComponent {

  public static class DemoResourceBarType extends ResourceBarComponentType {

    @Override
    public ResourceBarComponent construct(PlayerEntity player) {
      return new DemoResourceBar(this, player);
    }

    @Override
    public Identifier getID() {
      return RESOURCE_ID;
    }

    @Override
    public Ticker makeTicker(ResourceBarComponent rb) {
      return new Ticker(60, () -> {
        rb.updateValue(10);
      });
    }
    
    @Override
    public Color getColor() {
      return Color.green;
    }

    @Override
    public ResourceVisibility getVisibility() {
      return ResourceVisibility.HUD;
    }
    
    @Override
    protected long getDefaultMax() {
      return 128;
    }

    @Override
    protected long getDefaultValue() {
      return 0;
    }
    
  }
  
  public static final Identifier RESOURCE_ID = new Identifier("cottonrpg", "demo_resource");
    
  public DemoResourceBar(ResourceBarComponentType type, PlayerEntity owner) {
    super(type, owner);
  }

  @Override
  public Identifier getID() {
    return RESOURCE_ID;
  }
}
