package com.aurus.tinytactics;

import com.aurus.tinytactics.components.RulerMap;
import com.aurus.tinytactics.components.RulerMapPayload;
import com.aurus.tinytactics.render.RenderManager;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;

public class TinyTacticsClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		RenderManager.getManager().init();

		ClientPlayNetworking.registerGlobalReceiver(RulerMapPayload.ID, TinyTacticsClient::receiveRulerMapPacket);

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ServerHandler.broadcastPositions();
        });
	}

	public static void receiveRulerMapPacket(RulerMapPayload payload, ClientPlayNetworking.Context context) {
		ClientWorld world = context.client().world;

		if (world == null) {
			return;
		}

		RulerMap map = payload.map();
		RenderManager.getManager().updateMap(map);

		TinyTactics.LOGGER.info("Positions Received");
	}
}
