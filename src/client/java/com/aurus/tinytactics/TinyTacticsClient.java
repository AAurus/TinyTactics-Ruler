package com.aurus.tinytactics;

import com.aurus.tinytactics.data.ColorProviders;
import com.aurus.tinytactics.data.TacticsRulerMap;
import com.aurus.tinytactics.data.TacticsRulerMapPayload;
import com.aurus.tinytactics.render.RenderManager;
import com.aurus.tinytactics.render.blocks.ActorMarkerBlockRenderer;
import com.aurus.tinytactics.registry.BlockRegistrar;
import com.aurus.tinytactics.registry.ItemRegistrar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.world.ClientWorld;

public class TinyTacticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderManager.getManager().init();

        ClientPlayNetworking.registerGlobalReceiver(TacticsRulerMapPayload.ID,
                TinyTacticsClient::receiveRulerMapPacket);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ServerHandler.broadcastPositions();
        });

        ColorProviderRegistry.ITEM.register(ColorProviders::getItemColor, ItemRegistrar.SIMPLE_DYEABLE_ITEMS);
        ColorProviderRegistry.BLOCK.register(ColorProviders::getBlockEntityColor,
                BlockRegistrar.SIMPLE_DYEABLE_BLOCKS);

        BlockEntityRendererFactories.register(BlockRegistrar.ACTOR_MARKER_BLOCK_ENTITY, ActorMarkerBlockRenderer::new);
    }

    public static void receiveRulerMapPacket(TacticsRulerMapPayload payload, ClientPlayNetworking.Context context) {
        ClientWorld world = context.client().world;

        if (world == null) {
            return;
        }

        TacticsRulerMap map = payload.map();
        RenderManager.getManager().updateMap(map);

        TinyTactics.LOGGER.info("Positions Received");
    }
}
