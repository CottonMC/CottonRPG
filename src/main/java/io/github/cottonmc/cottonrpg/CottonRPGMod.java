package io.github.cottonmc.cottonrpg;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import io.github.cottonmc.cottonrpg.commands.ClassGetCommand;
import io.github.cottonmc.cottonrpg.commands.ClassSetCommand;
import io.github.cottonmc.cottonrpg.commands.MainCommand;
import io.github.cottonmc.cottonrpg.components.IClassComponent;
import io.github.cottonmc.cottonrpg.demo.DemoClass;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.IdentifierArgumentType;
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
          .then(
            CommandManager.literal("class")
              .then(
              CommandManager.argument("classname", IdentifierArgumentType.identifier())
                .then(
                  CommandManager.literal("get")
                    .executes(new ClassGetCommand())
                )
                .then(
                  CommandManager.literal("set")
                    .then(
                      CommandManager.argument("level", IntegerArgumentType.integer())
                        .executes(new ClassSetCommand())
                    )
                )
              )
            )
      );
    });
    
    ClassRegistry.register(DemoClass.CLASS_ID, DEMO_CLASS, (PlayerEntity p) -> new DemoClass(p));
  }
}
