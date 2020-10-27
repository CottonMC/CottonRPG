package io.github.cottonmc.cottonrpg;

import com.mojang.serialization.Lifecycle;
import io.github.cottonmc.cottonrpg.commands.CottonRPGCommands;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClass;
import io.github.cottonmc.cottonrpg.data.rpgclass.SimpleCharacterClass;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResource;
import io.github.cottonmc.cottonrpg.data.rpgresource.SimpleCharacterResource;
import io.github.cottonmc.cottonrpg.data.rpgskill.CharacterSkill;
import io.github.cottonmc.cottonrpg.data.rpgskill.SimpleCharacterSkill;
import io.github.cottonmc.cottonrpg.mixin.RegistryAccessor;
import io.github.cottonmc.cottonrpg.prereq.Prerequisite;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class CottonRPG implements ModInitializer {
	public static final String MODID = "cottonrpg";
	public static final Logger LOGGER = LogManager.getLogger("CottonRPG");
	public static final RegistryKey<Registry<CharacterClass>> CLASS_KEY = RegistryKey.ofRegistry(id("character_class"));
	public static final Registry<CharacterClass> CLASSES = RegistryAccessor.create(CLASS_KEY, Lifecycle.stable(), () -> null);
	public static final RegistryKey<Registry<CharacterResource>> RESOURCE_KEY = RegistryKey.ofRegistry(id("character_resource"));
	public static final Registry<CharacterResource> RESOURCES = RegistryAccessor.create(RESOURCE_KEY, Lifecycle.stable(), () -> null);
	public static final RegistryKey<Registry<CharacterSkill>> SKILL_KEY = RegistryKey.ofRegistry(id("character_skill"));
	public static final Registry<CharacterSkill> SKILLS = RegistryAccessor.create(SKILL_KEY, Lifecycle.stable(), () -> null);
	public static final boolean CCA_ITEM_LOADED = FabricLoader.getInstance().isModLoaded("cardinal-components-item");

	//TODO: figure out how we want to do config
	public static CottonRPGConfig config = new CottonRPGConfig();

	@NotNull
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}

	@Override
	public void onInitialize() {
		CottonRPGCommands.init();

		//only have these in for testing / sample purposes
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Identifier test_class = id("test_class");
			Registry.register(CLASSES, test_class, new SimpleCharacterClass(5, (level) -> Prerequisite.none(), (level, player) -> {
			}));
			Identifier test_resource = id("test_resource");
			Registry.register(RESOURCES, test_resource, new SimpleCharacterResource(0, 20, 4, 5, 0x00FF00, CharacterResource.ResourceVisibility.HUD));
			Identifier test_skill = id("test_skill");
			Registry.register(SKILLS, test_skill, new SimpleCharacterSkill(100, Prerequisite.none(), (player, target) -> {
				player.sendMessage(new LiteralText("Test success!"), false);
				return true;
			}));
		}
	}
}
