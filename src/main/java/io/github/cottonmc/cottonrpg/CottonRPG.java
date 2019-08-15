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

	//TODO: figure out how we want to do config
	public static CottonRPGConfig config = new CottonRPGConfig();

	@Override
	public void onInitialize() {
		CottonRPGNetworking.init();
		CottonRPGCommands.init();

		Identifier test_class = new Identifier(MODID, "test_class");
		Registry.register(CLASSES, test_class, new SimpleCharacterClass(5));
		Identifier test_resource = new Identifier(MODID, "test_resource");
		Registry.register(RESOURCES, test_resource, new SimpleCharacterResource(0L, 20L, 4L, 5, 0x00FF00, CharacterResource.ResourceVisibility.HUD));
		Identifier test_skill = new Identifier(MODID, "test_skill");
		Registry.register(SKILLS, test_skill, new SimpleCharacterSkill(100, new Prerequisite.True(), (player, target) -> {
			player.sendMessage(new LiteralText("Test success!"));
			return true;
		}));
	}
}
