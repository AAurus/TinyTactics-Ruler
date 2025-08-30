package com.aurus.tinytactics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aurus.tinytactics.data.RulerMap;
import com.aurus.tinytactics.data.RulerMapPayload;
import com.aurus.tinytactics.registry.DataRegistrar;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ServerHandler {
    private static List<ServerWorld> currentServerWorlds = new ArrayList<>();

    public static void init() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            currentServerWorlds.add(world);
        });
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            currentServerWorlds.remove(world);
        });
    }

    public static void broadcastPositions() {

        int numPlayers = 0;

        for (ServerWorld world : currentServerWorlds) {

            RulerMapPayload payload = new RulerMapPayload(
                    world.getAttachedOrCreate(DataRegistrar.ALL_RULER_POSITIONS, () -> RulerMap.DEFAULT));

            Collection<ServerPlayerEntity> players = PlayerLookup.world(world);
            numPlayers += players.size();

            for (ServerPlayerEntity serverPlayer : players) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }

        }

        TinyTactics.LOGGER.info("Positions broadcasted to " + numPlayers + " players");
    }

    public static void setPositions(World world, RulerMap newMap) {
        world.setAttached(DataRegistrar.ALL_RULER_POSITIONS, newMap);
    }

}
