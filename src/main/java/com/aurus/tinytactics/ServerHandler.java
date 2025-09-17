package com.aurus.tinytactics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aurus.tinytactics.data.TacticsRulerMap;
import com.aurus.tinytactics.data.TacticsRulerMapPayload;
import com.aurus.tinytactics.data.TacticsShapeMap;
import com.aurus.tinytactics.data.TacticsShapeMapPayload;
import com.aurus.tinytactics.registry.DataRegistrar;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
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

    public static void broadcastRulerData() {
        broadcastPayload((world) -> new TacticsRulerMapPayload(
                world.getAttachedOrCreate(DataRegistrar.TACTICS_RULER_POSITIONS, () -> TacticsRulerMap.DEFAULT)),
                "Positions");
    }

    public static void broadcastShapeData() {

        broadcastPayload((world) -> new TacticsShapeMapPayload(
                world.getAttachedOrCreate(DataRegistrar.TACTICS_SHAPES, () -> TacticsShapeMap.DEFAULT)), "Shapes");
    }

    public static void broadcastPayload(PayloadFactory factory, String name) {
        int numPlayers = 0;

        for (ServerWorld world : currentServerWorlds) {

            CustomPayload payload = factory.createPayload(world);

            Collection<ServerPlayerEntity> players = PlayerLookup.world(world);
            numPlayers += players.size();

            for (ServerPlayerEntity serverPlayer : players) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }

        }

        TinyTactics.LOGGER.info(name + " broadcasted to " + numPlayers + " players");
    }

    public static void setPositions(World world, TacticsRulerMap newMap) {
        world.setAttached(DataRegistrar.TACTICS_RULER_POSITIONS, newMap);
    }

    public static void setShapes(World world, TacticsShapeMap newMap) {
        world.setAttached(DataRegistrar.TACTICS_SHAPES, newMap);
    }

    public static interface PayloadFactory {
        CustomPayload createPayload(World world);
    }
}
