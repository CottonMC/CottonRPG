package io.github.cottonmc.cottonrpg.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.cottonmc.cottonrpg.CottonRPG;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

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
                      CommandManager.argument("classname", ClassArgumentType.clazz())
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
                           CommandManager.literal("take")
                        .executes(new ClassRemoveCommand())
                   )
              )
         )
         .then(
                 CommandManager.literal("resource")
              .then(
                      CommandManager.argument("resourcename", ResourceArgumentType.resource())
                   .then(
                           CommandManager.literal("give")
                        .executes(new ResourceGiveCommand())
                   )
                   .then(
                           CommandManager.literal("take")
                        .executes(new ResourceRemoveCommand())
                   )
              )
         )
    ));
  }

  public static class ClassArgumentType extends IdentifierArgumentType {
    public static ClassArgumentType clazz() {
      return new ClassArgumentType();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestIdentifiers(CottonRPG.CLASSES.getIds(), builder);
    }
  }

  public static class ResourceArgumentType extends IdentifierArgumentType {
    public static ResourceArgumentType resource() {
      return new ResourceArgumentType();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestIdentifiers(CottonRPG.RESOURCES.getIds(), builder);
    }
  }
}
