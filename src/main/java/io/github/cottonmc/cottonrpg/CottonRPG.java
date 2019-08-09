package io.github.cottonmc.cottonrpg;

import io.github.cottonmc.cottonrpg.commands.CottonRPGCommands;
import io.github.cottonmc.cottonrpg.data.CharacterClass;
import io.github.cottonmc.cottonrpg.data.CharacterResource;
import io.github.cottonmc.cottonrpg.data.SimpleCharacterClass;
import io.github.cottonmc.cottonrpg.data.SimpleCharacterResource;
import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class CottonRPG implements ModInitializer {
  public static final String MODID = "cottonrpg";
  public static final Registry<CharacterClass> CLASSES = new SimpleRegistry<>();
  public static final Registry<CharacterResource> RESOURCES = new SimpleRegistry<>();
  //TODO: figure out how we want to do config
  public static CottonRPGConfig config = new CottonRPGConfig();

  @Override
  public void onInitialize() {
    CottonRPGNetworking.init();
    CottonRPGCommands.init();

    Identifier test_class = new Identifier(MODID, "test_class");
    Registry.register(CLASSES, test_class, new SimpleCharacterClass(test_class, 5));
    Identifier test_resource = new Identifier(MODID, "test_resource");
    Registry.register(RESOURCES, test_resource, new SimpleCharacterResource(test_resource, 15L, 20L, 4L, 0x00FF00, CharacterResource.ResourceVisibility.HUD));
    
  }
}
