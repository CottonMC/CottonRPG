package io.github.cottonmc.cottonrpg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.cottonmc.cottonrpg.CharacterDataHolder;
import io.github.cottonmc.cottonrpg.components.ClassComponent;
import io.github.cottonmc.cottonrpg.components.ResourceBarComponent;
import io.github.cottonmc.cottonrpg.util.RPGPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

@Mixin(PlayerEntity.class)
public class PlayerEntityRPGMixin implements RPGPlayer {
  
  CharacterDataHolder cottonRPGCharData;
  
  @Inject(at = @At("RETURN"), method = "<init>")
  private void init(CallbackInfo ci) {
    cottonRPGCharData = new CharacterDataHolder((PlayerEntity) (Object) this);
  }
  
  @Inject(at = @At("RETURN"), method = "tick")
  private void tick(CallbackInfo ci) {
    cottonRPGCharData.resourceBars.forEach((id, rb) -> {
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
          ClassComponent cc = cottonRPGCharData.classes.get(id);
          if (cc == null) {
            cc = cottonRPGCharData.classes.enable(id);
          }
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
          ResourceBarComponent rbc = cottonRPGCharData.resourceBars.get(id);
          if (rbc == null) {
            rbc = cottonRPGCharData.resourceBars.enable(id);
          }
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
    cottonRPGCharData.classes.forEach((id, cc) -> {
      CompoundTag cclass = new CompoundTag();
      cclass = cc.toTag(cclass);
      cclasses.put(id.getNamespace() + ":" + id.getPath(), cclass);
    });
    crpg.put("classes", cclasses);
    
    CompoundTag cresourceBars = new CompoundTag();
    cottonRPGCharData.resourceBars.forEach((id, rbc) -> {
      CompoundTag cresourceBar = new CompoundTag();
      cresourceBar = rbc.toTag(cresourceBar);
      cresourceBars.put(id.getNamespace() + ":" + id.getPath(), cresourceBar);
    });
    crpg.put("resourceBars", cresourceBars);
    
    tag.put("cottonrpg", crpg);
  }
  
  @Override
  public CharacterDataHolder cottonRPGGetCharacterDataHolder() {
    return cottonRPGCharData;
  }
  
}
