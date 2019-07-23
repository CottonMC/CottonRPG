package io.github.cottonmc.cottonrpg.mixin;

import io.github.cottonmc.cottonrpg.data.CharacterClassEntry;
import io.github.cottonmc.cottonrpg.data.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.CharacterResourceEntry;
import io.github.cottonmc.cottonrpg.data.CharacterResources;
import io.github.cottonmc.cottonrpg.util.CharacterDataHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityRPGMixin implements CharacterDataHolder {
  private CharacterClasses classes = new CharacterClasses();
  private CharacterResources resources = new CharacterResources();

  @Inject(at = @At("RETURN"), method = "<init>")
  private void init(CallbackInfo ci) {
    //TODO: add listeners for sync here
  }
  
  @Inject(at = @At("RETURN"), method = "tick")
  private void tick(CallbackInfo ci) {
    resources.forEach((id, rb) -> rb.tick());
  }
  
  @Inject(at = @At(value = "INVOKE"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
  private void fromTag(CompoundTag tag, CallbackInfo ci) {
    if (!tag.containsKey("cottonrpg")) return;
    
    CompoundTag crpg = tag.getCompound("CottonRPG");
    
    if (crpg.containsKey("Classes")) {
      CompoundTag cclasses = crpg.getCompound("Classes");
      cclasses.getKeys().forEach(k -> {
        try {
          Identifier id = new Identifier(k);
          classes.giveIfAbsent(new CharacterClassEntry(id));
          CharacterClassEntry cc = classes.get(id);
          CompoundTag cclass = cclasses.getCompound(k);
          cc.fromTag(cclass);
        } catch (Exception e) {
          // todo: report warning
        }
      });
    }
    
    if (crpg.containsKey("Resources")) {
      CompoundTag cresourceBars = crpg.getCompound("Resources");
      cresourceBars.getKeys().forEach(k -> {
        try {
          Identifier id = new Identifier(k);
          resources.giveIfAbsent(new CharacterResourceEntry(id));
          CharacterResourceEntry rbc = resources.get(id);
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
      CompoundTag cclass = cc.toTag();
      cclasses.put(id.getNamespace() + ":" + id.getPath(), cclass);
    });
    crpg.put("Classes", cclasses);
    
    CompoundTag cresourceBars = new CompoundTag();
    resources.forEach((id, rbc) -> {
      CompoundTag cresourceBar = rbc.toTag();
      cresourceBars.put(id.getNamespace() + ":" + id.getPath(), cresourceBar);
    });
    crpg.put("Resources", cresourceBars);
    
    tag.put("CottonRPG", crpg);
  }

  @Override
  public CharacterClasses crpg_getClasses() {
    return classes;
  }

  @Override
  public CharacterResources crpg_getResources() {
    return resources;
  }
}
