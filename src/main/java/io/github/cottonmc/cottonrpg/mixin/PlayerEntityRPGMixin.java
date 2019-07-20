package io.github.cottonmc.cottonrpg.mixin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.cottonmc.cottonrpg.ClassRegistry;
import io.github.cottonmc.cottonrpg.ResourceBarRegistry;
import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import io.github.cottonmc.cottonrpg.util.RPGPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

@Mixin(PlayerEntity.class)
public class PlayerEntityRPGMixin implements RPGPlayer {
  private ConcurrentHashMap<Identifier, ClassComponent> cottonRPGClasses;
  private ConcurrentHashMap<Identifier, ResourceBarComponent> cottonRPGResourceBars; 
  
  @Inject(at = @At("RETURN"), method = "<init>")
  private void init(CallbackInfo ci) {
    cottonRPGClasses = new ConcurrentHashMap<>();
    ClassRegistry.INSTANCE.forEach(cct -> {
      cottonRPGClasses.put(cct.getID(), cct.construct((PlayerEntity) (Object) this));
    });
    
    cottonRPGResourceBars = new ConcurrentHashMap<>();
    ResourceBarRegistry.INSTANCE.forEach(rbct -> {
      cottonRPGResourceBars.put(rbct.getID(), rbct.construct((PlayerEntity) (Object) this));
    });
  }
  
  @Inject(at = @At("RETURN"), method = "tick")
  private void tick(CallbackInfo ci) {
    cottonRPGResourceBars.forEach((id, rb) -> {
      rb.tick();
    });
  }
  
  @Inject(at = @At(value = "INVOKE"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
  private void fromTag(CompoundTag tag, CallbackInfo ci) {
    if (!tag.containsKey("cottonrpg")) return;
    
    CompoundTag crpg = tag.getCompound("cottonrpg");
    
    if (crpg.containsKey("classes")) {
      CompoundTag cclasses = crpg.getCompound("classes");
      cclasses.getKeys().forEach(k -> {
        try {
          Identifier id = new Identifier(k);
          ClassComponent cc = cottonRPGClasses.get(id);
          CompoundTag cclass = cclasses.getCompound(k);
          cc.fromTag(cclass);
        } catch (Exception e) {
          // todo: report warning
        }
      });
    }
    
    if (crpg.containsKey("resourceBars")) {
      CompoundTag cresourceBars = crpg.getCompound("resourceBars");
      cresourceBars.getKeys().forEach(k -> {
        try {
          Identifier id = new Identifier(k);
          ResourceBarComponent rbc = cottonRPGResourceBars.get(id);
          CompoundTag cresourceBar = cresourceBars.getCompound(k);
          rbc.fromTag(cresourceBar);
        } catch (Exception e) {
          // todo: report warning
        }
      });
    }
    
  }
  
  @Inject(at = @At(value = "INVOKE"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
  private void toTag(CompoundTag tag, CallbackInfo ci) {    
    CompoundTag crpg = new CompoundTag();
    
    CompoundTag cclasses = new CompoundTag();
    cottonRPGClasses.forEach((id, cc) -> {
      CompoundTag cclass = new CompoundTag();
      cclass = cc.toTag(cclass);
      cclasses.put(id.getNamespace() + ":" + id.getPath(), cclass);
    });
    crpg.put("classes", cclasses);
    
    CompoundTag cresourceBars = new CompoundTag();
    cottonRPGResourceBars.forEach((id, rbc) -> {
      CompoundTag cresourceBar = new CompoundTag();
      cresourceBar = rbc.toTag(cresourceBar);
      cresourceBars.put(id.getNamespace() + ":" + id.getPath(), cresourceBar);
    });
    crpg.put("resourceBars", cresourceBars);
    
    tag.put("cottonrpg", crpg);
  }
  
  @Override
  public ClassComponent getRPGClass(Identifier id) {
    return cottonRPGClasses.get(id);
  }
  
  @Override
  public void forEachRPGClass(BiConsumer<Identifier, ClassComponent> cons) {
    cottonRPGClasses.forEach(cons);
  }
  
  @Override
  public ResourceBarComponent getRPGResourceBar(Identifier id) {
    return cottonRPGResourceBars.get(id);
  }
  
  @Override
  public void forEachRPGResourceBar(BiConsumer<Identifier, ResourceBarComponent> cons) {
    cottonRPGResourceBars.forEach(cons);
  }

}
