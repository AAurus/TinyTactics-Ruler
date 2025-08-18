package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.components.BlockPosList;
import com.aurus.tinytactics.components.BlockPosMap;
import com.aurus.tinytactics.components.BlockPosMapPayload;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class DataRegistrar {

    private static ServerWorld currentServerWorld = null;
    private static BlockPosMap currentAllRulerPos = BlockPosMap.DEFAULT;

    public static final ComponentType<BlockPosList> RULER_POSITIONS = Registry.register(Registries.DATA_COMPONENT_TYPE, 
        Identifier.of(TinyTactics.MOD_ID, "ruler_positions"), 
        ComponentType.<BlockPosList>builder().codec(BlockPosList.CODEC).build());

    public static final AttachmentType<BlockPosMap> ALL_RULER_POSITIONS = AttachmentRegistry.create(
        Identifier.of(TinyTactics.MOD_ID, "all_ruler_positions"), 
            builder -> builder.initializer(() -> BlockPosMap.DEFAULT).persistent(BlockPosMap.CODEC).syncWith(BlockPosMap.PACKET_CODEC, AttachmentSyncPredicate.all()));

    public static void registerAll() {
        PayloadTypeRegistry.playS2C().register(BlockPosMapPayload.ID, BlockPosMapPayload.CODEC);

        ServerWorldEvents.LOAD.register((server, world) -> {
            currentServerWorld = world;
            currentAllRulerPos = world.getAttachedOrCreate(ALL_RULER_POSITIONS,() -> BlockPosMap.DEFAULT);
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            BlockPosMapPayload payload = new BlockPosMapPayload(currentAllRulerPos);

            for (ServerPlayerEntity serverPlayer : PlayerLookup.world(currentServerWorld)) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        });
    }
}
