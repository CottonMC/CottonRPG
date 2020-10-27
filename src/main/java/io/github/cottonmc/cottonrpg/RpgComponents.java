package io.github.cottonmc.cottonrpg;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResources;
import io.github.cottonmc.cottonrpg.data.rpgskill.CharacterSkills;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;

public final class RpgComponents implements EntityComponentInitializer {
	public static final ComponentKey<CharacterClasses> CLASSES = ComponentRegistry.getOrCreate(CottonRPG.id("classes"), CharacterClasses.class);
	public static final ComponentKey<CharacterResources> RESOURCES = ComponentRegistry.getOrCreate(CottonRPG.id("resources"), CharacterResources.class);
	public static final ComponentKey<CharacterSkills> SKILLS = ComponentRegistry.getOrCreate(CottonRPG.id("skills"), CharacterSkills.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(CLASSES, CharacterClasses.Impl::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(RESOURCES, CharacterResources.Impl::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(SKILLS, CharacterSkills.Impl::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
