package io.github.cottonmc.cottonrpg.mixin;

import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import io.github.cottonmc.cottonrpg.components.SimpleClassComponent;
import io.github.cottonmc.cottonrpg.components.SimpleResourceBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

  public ConcurrentHashMap<Identifier, SimpleClassComponent> classes;
  public ConcurrentHashMap<Identifier, SimpleResourceBarComponent> resourceBars; 
  
  @Inject(at = @At("HEAD"), method = "init()V")
  private void init() {
    classes = new ConcurrentHashMap<>();
    resourceBars = new ConcurrentHashMap<>();
  }
  
  @Inject(at = @At("RETURN"), method = "tick()V")
  private void tick() {
    
  }

}
