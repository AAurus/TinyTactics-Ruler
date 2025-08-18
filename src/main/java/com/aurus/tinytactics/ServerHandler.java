package com.aurus.tinytactics;

import com.aurus.tinytactics.components.BlockPosMap;
import com.aurus.tinytactics.components.BlockPosMapPayload;
import com.aurus.tinytactics.registry.DataRegistrar;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ServerHandler {
    private static ServerWorld currentServerWorld = null;
    private static BlockPosMap currentAllRulerPos = BlockPosMap.DEFAULT;

    public static void init() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            currentServerWorld = world;
            currentAllRulerPos = world.getAttachedOrCreate(DataRegistrar.ALL_RULER_POSITIONS,() -> BlockPosMap.DEFAULT);
        });
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            currentServerWorld = null;
            currentAllRulerPos = BlockPosMap.DEFAULT;
        });
    }

    public static void broadcastPositions() {
        BlockPosMapPayload payload = new BlockPosMapPayload(currentAllRulerPos);

        for (ServerPlayerEntity serverPlayer : PlayerLookup.world(currentServerWorld)) {
            ServerPlayNetworking.send(serverPlayer, payload);
        }
    }
}
