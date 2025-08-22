package com.aurus.tinytactics;

import com.aurus.tinytactics.items.ColorProviders;

import com.aurus.tinytactics.components.RulerMap;
import com.aurus.tinytactics.components.RulerMapPayload;
import com.aurus.tinytactics.render.RenderManager;
import com.aurus.tinytactics.registry.ItemRegistrar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.world.ClientWorld;

public class TinyTacticsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		RenderManager.getManager().init();

		ClientPlayNetworking.registerGlobalReceiver(RulerMapPayload.ID, TinyTacticsClient::receiveRulerMapPacket);

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			ServerHandler.broadcastPositions();
		});

		ColorProviderRegistry.ITEM
				.register(ColorProviders::getColor,
						ItemRegistrar.SIMPLE_DYEABLE_ITEMS);
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
