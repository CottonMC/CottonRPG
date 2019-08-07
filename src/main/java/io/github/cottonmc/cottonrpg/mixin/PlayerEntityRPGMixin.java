package io.github.cottonmc.cottonrpg.mixin;

import io.github.cottonmc.cottonrpg.data.CharacterClassEntry;
import io.github.cottonmc.cottonrpg.data.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.CharacterResourceEntry;
import io.github.cottonmc.cottonrpg.data.CharacterResources;
import io.github.cottonmc.cottonrpg.util.CharacterDataHolder;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityRPGMixin implements CharacterDataHolder {
  private CharacterClasses classes = new CharacterClasses((PlayerEntity)(Object)this);
  private CharacterResources resources = new CharacterResources();
  
  @Inject(at = @At("RETURN"), method = "tick")
  private void tick(CallbackInfo ci) {
    if ((Object)this instanceof ServerPlayerEntity) {
      resources.sync((ServerPlayerEntity)(Object)this);
    }
    resources.forEach((id, resource) -> resource.tick());
  }
  
  @Inject(at = @At(value = "INVOKE"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
  private void fromTag(CompoundTag tag, CallbackInfo ci) {
    if (!tag.containsKey("CottonRPG")) return;
    
    CompoundTag crpg = tag.getCompound("CottonRPG");
    if (crpg==null) {
    	System.out.println("CottonRPG tag was the wrong type! ("+tag.getTag("CottonRPG").getClass().getSimpleName()+")");
    	return;
    } else {
    	System.out.println("Unpacking tag "+crpg.asString());
    }
    
    if (crpg.containsKey("Classes")) {
      CompoundTag cclasses = crpg.getCompound("Classes");
      for (String key : cclasses.getKeys()) {
        if (cclasses.getType(key) == NbtType.COMPOUND) try {
          Identifier id = new Identifier(key);
          this.classes.giveIfAbsent(new CharacterClassEntry(id, (PlayerEntity)(Object)this));
          CharacterClassEntry entry = this.classes.get(id);
          CompoundTag cclass = cclasses.getCompound(key);
          entry.fromTag(cclass);
        } catch (Exception e) {
          System.out.println("[CottonRPG] Couldn't read class!");
        }
      }
    }
    
    if (crpg.containsKey("Resources")) {
      CompoundTag cresources = crpg.getCompound("Resources");
      for (String key : cresources.getKeys()) {
        if (cresources.getType(key) == NbtType.COMPOUND) try {
          Identifier id = new Identifier(key);
          this.resources.giveIfAbsent(new CharacterResourceEntry(id));
          CharacterResourceEntry entry = this.resources.get(id);
          CompoundTag cresourceBar = cresources.getCompound(key);
          entry.fromTag(cresourceBar);
        } catch (Exception e) {
          System.out.println("[CottonRPG] Couldn't read resource!");
          e.printStackTrace();
        }
      }
    }
    
  }
  
  @Inject(at = @At(value = "INVOKE"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
  private void toTag(CompoundTag tag, CallbackInfo ci) {    
    CompoundTag crpg = new CompoundTag();
    
    CompoundTag cclasses = new CompoundTag();
    classes.forEach((id, entry) -> {
      CompoundTag cclass = entry.toTag();
      cclasses.put(id.toString(), cclass);
    });
    crpg.put("Classes", cclasses);
    
    CompoundTag cresourceBars = new CompoundTag();
    resources.forEach((id, entry) -> {
      CompoundTag cresourceBar = entry.toTag();
      cresourceBars.put(id.toString(), cresourceBar);
    });
    crpg.put("Resources", cresourceBars);
    
    System.out.println("Saving down tag "+crpg.asString());
    
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
