package com.aurus.tinytactics;

import org.joml.Matrix4f;

import com.aurus.tinytactics.render.VisualManager;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.client.MinecraftClient;

public class TinyTacticsClient implements ClientModInitializer {
    @Override
	public void onInitializeClient() {
		VisualManager.init();
	}

	public static void renderOverlay(MatrixStack matrices, BufferBuilderStorage bufferBuilders, Camera camera, Matrix4f projection) {
        final MinecraftClient clientInstance = MinecraftClient.getInstance();
		if (clientInstance.player != null) {
			final DimensionType dimension = clientInstance.player.getWorld().getDimension();
			VisualManager.renderLines(dimension, matrices, bufferBuilders, camera, projection);
		}
    }
}
