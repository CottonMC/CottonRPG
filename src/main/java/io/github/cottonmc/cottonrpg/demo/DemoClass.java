package io.github.cottonmc.cottonrpg.demo;

import io.github.cottonmc.cottonrpg.components.ClassComponentType;
import io.github.cottonmc.cottonrpg.components.SimpleClassComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DemoClass extends SimpleClassComponent {
  public static class DemoClassType extends ClassComponentType {

    @Override
    public SimpleClassComponent construct(PlayerEntity player) {
      return new DemoClass(this, player);
    }

    @Override
    public Identifier getID() {
      return CLASS_ID;
    }
    
  }
  
  public DemoClass(ClassComponentType type, PlayerEntity player) {
    super(type, player);
  }

  public static final Identifier CLASS_ID = new Identifier("cottonrpg:demo_class");
  
  @Override
  public Identifier getID() {
    return CLASS_ID;
  }
}
