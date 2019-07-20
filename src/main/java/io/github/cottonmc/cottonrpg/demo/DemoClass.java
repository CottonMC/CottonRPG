package io.github.cottonmc.cottonrpg.demo;

import io.github.cottonmc.cottonrpg.components.SimpleClassComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DemoClass extends SimpleClassComponent {
  public DemoClass(PlayerEntity owner) {
    super(owner);
  }

  public static final Identifier CLASS_ID = new Identifier("cottonrpg:demo_class");
  
  @Override
  public Identifier getID() {
    return CLASS_ID;
  }
}
