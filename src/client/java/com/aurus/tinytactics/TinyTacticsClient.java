package com.aurus.tinytactics;

import com.aurus.tinytactics.components.BlockPosMap;
import com.aurus.tinytactics.components.BlockPosMapPayload;
import com.aurus.tinytactics.render.RenderManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;

public class TinyTacticsClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		RenderManager.getManager().init();

		ClientPlayNetworking.registerGlobalReceiver(BlockPosMapPayload.ID, TinyTacticsClient::receiveRulerMapPacket);

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ServerHandler.broadcastPositions();
        });
	}

	public static void receiveRulerMapPacket(BlockPosMapPayload payload, ClientPlayNetworking.Context context) {
		ClientWorld world = context.client().world;

		if (world == null) {
			return;
		}

		BlockPosMap map = payload.map();
		RenderManager.getManager().updateMap(map);

		TinyTactics.LOGGER.info("Positions Received");
	}
}
