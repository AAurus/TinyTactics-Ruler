package com.aurus.tinytactics.render.blocks;

import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActorMarkerBlockRenderer implements BlockEntityRenderer<ActorMarkerBlockEntity> {

    public ActorMarkerBlockRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(ActorMarkerBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState state = entity.getCachedState();
        MinecraftClient.getInstance().getBlockRenderManager().renderBlock(state, pos, world, matrices,
                vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), false,
                null);
    }

}
