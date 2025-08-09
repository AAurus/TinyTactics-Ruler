package com.aurus.tinytactics.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aurus.tinytactics.TinyTacticsClient;

import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(WorldRenderer.class)
public abstract class WorldRenderMixin {
    @Accessor
    abstract BufferBuilderStorage getBufferBuilders();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw()V", ordinal = 0))
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lightManager, Matrix4f proj, CallbackInfo callbackInfo) {
        TinyTacticsClient.renderOverlay(matrices, getBufferBuilders(), camera, proj);
    }
}
