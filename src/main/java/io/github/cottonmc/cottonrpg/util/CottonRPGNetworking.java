package io.github.cottonmc.cottonrpg.util;

import io.github.cottonmc.cottonrpg.data.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class CottonRPGNetworking {
	public static final Identifier ALL_CLASSES = new Identifier("cotton-rpg", "all_classes");
	public static final Identifier ALL_RESOURCES = new Identifier("cotton-rpg", "all_resources");
	public static final Identifier SINGLE_CLASS = new Identifier("cotton-rpg", "single_class");
	public static final Identifier SINGLE_RESOURCE = new Identifier("cotton-rpg", "single_resource");

	public static void init() {
		ClientSidePacketRegistry.INSTANCE.register(SINGLE_CLASS, ((ctx, buf) -> {
			Identifier id = buf.readIdentifier();
			CharacterClasses classes = CharacterData.get(ctx.getPlayer()).getClasses();
			classes.giveIfAbsent(new CharacterClassEntry(id, ctx.getPlayer()));
			CharacterClassEntry entry = classes.get(id);
			entry.setLevel(buf.readInt());
			entry.setExperience(buf.readInt());
		}));
		ClientSidePacketRegistry.INSTANCE.register(SINGLE_RESOURCE, ((ctx, buf) -> {
			Identifier id = buf.readIdentifier();
			CharacterResources resources = CharacterData.get(ctx.getPlayer()).getResources();
			resources.giveIfAbsent(new CharacterResourceEntry(id, ctx.getPlayer()));
			CharacterResourceEntry entry = resources.get(id);
			entry.setCurrent(buf.readLong());
			entry.setMax(buf.readLong());
		}));
	}

	public static void syncAllClasses(ServerPlayerEntity player, CharacterClasses data) {
		//TODO: impl when it's not 1 AM, iterate over all classes and put into buf
	}

	public static void syncAllResources(ServerPlayerEntity player, CharacterResources data) {
		//TODO: impl when it's not 1 AM, iterate over all resources and put into buf
	}

	public static void syncClassChange(ServerPlayerEntity player, CharacterClassEntry entry) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeIdentifier(entry.id);
		buf.writeInt(entry.getLevel());
		buf.writeInt(entry.getExperience());
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(SINGLE_CLASS, buf));
	}

	public static void syncResourceChange(ServerPlayerEntity player, CharacterResourceEntry entry) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeIdentifier(entry.id);
		buf.writeLong(entry.getCurrent());
		buf.writeLong(entry.getMax());
		player.networkHandler.sendPacket(new CustomPayloadS2CPacket(SINGLE_RESOURCE, buf));
	}
}
