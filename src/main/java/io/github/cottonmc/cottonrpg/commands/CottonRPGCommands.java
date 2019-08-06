package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CottonRPGCommands {
  public static void init() {
    CommandRegistry.INSTANCE.register(false, (CommandDispatcher<ServerCommandSource> cmd) -> cmd.register(CommandManager.literal("cottonrpg")
         .executes(new MainCommand())
         .then(
                 CommandManager.literal("classes")
              .executes(new ClassesCommand())
         )
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
                   .then(
                           CommandManager.literal("give")
                        .executes(new ClassGiveCommand())
                   )
                   .then(
                           CommandManager.literal("remove")
                        .executes(new ClassRemoveCommand())
                   )
              )
         )
         .then(
                 CommandManager.literal("resource")
              .then(
                      CommandManager.argument("resourcename", IdentifierArgumentType.identifier())
                   .then(
                           CommandManager.literal("give")
                        .executes(new ResourceGiveCommand())
                   )
                   .then(
                           CommandManager.literal("remove")
                        .executes(new ResourceRemoveCommand())
                   )
              )
         )
    ));
  }
}
