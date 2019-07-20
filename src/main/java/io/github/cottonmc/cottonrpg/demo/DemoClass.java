package io.github.cottonmc.cottonrpg.demo;

import io.github.cottonmc.cottonrpg.components.ClassComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DemoClass extends ClassComponent {
  public DemoClass(PlayerEntity owner) {
    super(owner);
  }

  public static final Identifier CLASS_ID = new Identifier("cottonrpg:demo_class");

  /*
  @Override
  public ComponentType<?> getComponentType() {
    return CottonRPGMod.DEMO_CLASS;
  }
  */
  
  @Override
  public Identifier getID() {
    return CLASS_ID;
  }
}
