package io.github.cottonmc.cottonrpg.mixin;

import java.util.concurrent.ConcurrentHashMap;

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
  private static final String KEY_COTTONRPG = "cottonrpg";
  private static final String KEY_CLASSES = "classes";
  private static final String KEY_RESOURCE_BARS = "resourceBars";

  private ConcurrentHashMap<Identifier, ClassComponent> classes;
  private ConcurrentHashMap<Identifier, ResourceBarComponent> resourceBars; 
  
  @Inject(at = @At("RETURN"), method = "<init>")
  private void init(CallbackInfo ci) {
    classes = new ConcurrentHashMap<>();
    ClassRegistry.INSTANCE.forEach(cct -> {
      classes.put(cct.getID(), cct.construct((PlayerEntity) (Object) this));
    });
    
    resourceBars = new ConcurrentHashMap<>();
    ResourceBarRegistry.INSTANCE.forEach(rbct -> {
      resourceBars.put(rbct.getID(), rbct.construct((PlayerEntity) (Object) this));
    });
  }
  
  @Inject(at = @At("RETURN"), method = "tick")
  private void tick(CallbackInfo ci) {
    resourceBars.forEach((id, rb) -> {
      rb.tick();
    });
  }
  
  @Inject(at = @At(value = "INVOKE"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
  private void fromTag(CompoundTag tag, CallbackInfo ci) {
    if (!tag.containsKey(KEY_COTTONRPG)) return;
    
    CompoundTag crpg = tag.getCompound(KEY_COTTONRPG);
    
    if (crpg.containsKey(KEY_CLASSES)) {
      CompoundTag cclasses = crpg.getCompound(KEY_CLASSES);
      cclasses.getKeys().forEach(k -> {
        try {
          Identifier id = new Identifier(k);
          ClassComponent cc = classes.get(id);
          CompoundTag cclass = cclasses.getCompound(k);
          cc.fromTag(cclass);
        } catch (Exception e) {
          // todo: report warning
        }
      });
    }
    
    if (crpg.containsKey(KEY_RESOURCE_BARS)) {
      CompoundTag cresourceBars = crpg.getCompound(KEY_RESOURCE_BARS);
      cresourceBars.getKeys().forEach(k -> {
        try {
          Identifier id = new Identifier(k);
          ResourceBarComponent rbc = resourceBars.get(id);
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
    classes.forEach((id, cc) -> {
      CompoundTag cclass = new CompoundTag();
      cclass = cc.toTag(cclass);
      cclasses.put(id.getNamespace() + ":" + id.getPath(), cclass);
    });
    crpg.put(KEY_CLASSES, cclasses);
    
    CompoundTag cresourceBars = new CompoundTag();
    resourceBars.forEach((id, rbc) -> {
      CompoundTag cresourceBar = new CompoundTag();
      cresourceBar = rbc.toTag(cresourceBar);
      cresourceBars.put(id.getNamespace() + ":" + id.getPath(), cresourceBar);
    });
    crpg.put(KEY_CLASSES, cresourceBars);
    
    tag.put(KEY_COTTONRPG, crpg);
  }
  
  @Override
  public ClassComponent getRPGClass(Identifier id) {
    return classes.get(id);
  }
  
  @Override
  public ResourceBarComponent getRPGResourceBar(Identifier id) {
    return resourceBars.get(id);
  }

}
