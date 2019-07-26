package io.github.cottonmc.cottonrpg;

import java.awt.Color;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import io.github.cottonmc.cottonrpg.commands.ClassGetCommand;
import io.github.cottonmc.cottonrpg.commands.ClassGiveCommand;
import io.github.cottonmc.cottonrpg.commands.ClassRemoveCommand;
import io.github.cottonmc.cottonrpg.commands.ClassSetCommand;
import io.github.cottonmc.cottonrpg.commands.ClassesCommand;
import io.github.cottonmc.cottonrpg.commands.MainCommand;
import io.github.cottonmc.cottonrpg.commands.ResourceGiveCommand;
import io.github.cottonmc.cottonrpg.commands.ResourceRemoveCommand;
import io.github.cottonmc.cottonrpg.data.CharacterClass;
import io.github.cottonmc.cottonrpg.data.CharacterResource;
import io.github.cottonmc.cottonrpg.data.SimpleCharacterClass;
import io.github.cottonmc.cottonrpg.data.SimpleCharacterResource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class CottonRPG implements ModInitializer {
  public static final Registry<CharacterClass> CLASSES = new SimpleRegistry<>();
  public static final Registry<CharacterResource> RESOURCES = new SimpleRegistry<>();

  @Override
  public void onInitialize() {
    
    CommandRegistry.INSTANCE.register(false, (CommandDispatcher<ServerCommandSource> cmd) -> {
      cmd.register(
        CommandManager.literal("cottonrpg")
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
      );
    });

    Identifier tcid = new Identifier("cotton-rpg", "test_class");
    Registry.register(CLASSES, tcid, new SimpleCharacterClass(tcid, 5));
    Identifier trid = new Identifier("cotton-rpg", "test_resource");
    Registry.register(RESOURCES, trid, new SimpleCharacterResource(trid, 16, 20, Color.GREEN, CharacterResource.ResourceVisibility.HUD));
    
  }
}
