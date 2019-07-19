package io.github.cottonmc.cottonrpg;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.cottonrpg.components.IClassComponent;
import io.github.cottonmc.cottonrpg.demo.DemoClass;
import io.github.cottonmc.cottonrpgcommands.MainCommand;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CottonRPGMod implements ModInitializer {  
  public static final ComponentType<IClassComponent> DEMO_CLASS =
      ComponentRegistry.INSTANCE.registerIfAbsent(DemoClass.CLASS_ID, IClassComponent.class);
  
  @Override
  public void onInitialize() {
    
    CommandRegistry.INSTANCE.register(false, (CommandDispatcher<ServerCommandSource> cmd) -> {
      cmd.register(
        CommandManager.literal("cottonrpg")
          .executes(new MainCommand())
      );
    });
    
    EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> {
      
    });
    
    ClassRegistry.register(DemoClass.CLASS_ID, DEMO_CLASS);
  }
}
