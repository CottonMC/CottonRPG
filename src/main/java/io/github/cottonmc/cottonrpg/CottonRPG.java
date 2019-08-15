package io.github.cottonmc.cottonrpg;

import io.github.cottonmc.cottonrpg.commands.CottonRPGCommands;
import io.github.cottonmc.cottonrpg.data.*;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import io.github.cottonmc.cottonrpg.util.CottonRPGNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class CottonRPG implements ModInitializer {
  public static final String MODID = "cottonrpg";
  public static final Registry<CharacterClass> CLASSES = new SimpleRegistry<>();
  public static final Registry<CharacterResource> RESOURCES = new SimpleRegistry<>();
  public static final Registry<CharacterSkill> SKILLS = new SimpleRegistry<>();

  //TODO: figure out how we want to do config-wise
  public static CottonRPGConfig config = new CottonRPGConfig();

  public static CharacterClass TEST_CLASS;
  public static CharacterResource TEST_RESOURCE;
  public static CharacterSkill TEST_SKILL;

  @Override
  public void onInitialize() {
    CottonRPGNetworking.init();
    CottonRPGCommands.init();

    Identifier test_class = new Identifier(MODID, "test_class");
    TEST_CLASS = Registry.register(CLASSES, test_class, new SimpleCharacterClass(5));
    Identifier test_resource = new Identifier(MODID, "test_resource");
    TEST_RESOURCE = Registry.register(RESOURCES, test_resource, new SimpleCharacterResource(15L, 20L, 4L, 0x00FF00, CharacterResource.ResourceVisibility.HUD));
    Identifier test_skill = new Identifier(MODID, "test_skill");
    TEST_SKILL = Registry.register(SKILLS, test_skill, new SimpleCharacterSkill(100, new Prerequisite.True(), (player, target) -> {
      player.sendMessage(new LiteralText("Test success!"));
      return true;
    }));
  }
}
