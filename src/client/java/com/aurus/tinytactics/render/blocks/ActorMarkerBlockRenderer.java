package com.aurus.tinytactics.render.blocks;

import org.joml.Vector3f;

import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlock;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlockEntity;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerRotationHelper;
import com.aurus.tinytactics.data.ActorMarkerInventory;
import com.aurus.tinytactics.data.ItemAttachmentPosition;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ActorMarkerBlockRenderer implements BlockEntityRenderer<ActorMarkerBlockEntity> {

    private static final ItemAttachmentPosition HEAD_POSITION = new ItemAttachmentPosition(
            new Vector3f(8.0F, 11.0F, 8.0F), new Vector3f(0.5F,
                    0.5F,
                    0.5F),
            new EulerAngle(0, 0, 0), ModelTransformationMode.HEAD,
            ActorMarkerInventory.HEAD_KEY);
    private static final ItemAttachmentPosition LEFT_HAND_POSITION = new ItemAttachmentPosition(
            new Vector3f(5.0F, 4.0F, 6.5F),
            new Vector3f(0.5F,
                    0.5F,
                    0.5F),
            new EulerAngle(40, 20, -90), ModelTransformationMode.THIRD_PERSON_LEFT_HAND,
            ActorMarkerInventory.LEFT_HAND_KEY,
            true, true);
    private static final ItemAttachmentPosition RIGHT_HAND_POSITION = new ItemAttachmentPosition(
            new Vector3f(11.0F, 4.0F, 6.5F),
            new Vector3f(0.5F,
                    0.5F,
                    0.5F),
            new EulerAngle(40, -20, 90), ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
            ActorMarkerInventory.RIGHT_HAND_KEY,
            true);
    private static final ItemAttachmentPosition ATTACHMENT_POSITION = new ItemAttachmentPosition(
            new Vector3f(8.0F, 7.5F, 12F),
            new Vector3f(0.4F,
                    0.4F,
                    0.4F),
            new EulerAngle(0, 180, 0), ModelTransformationMode.FIXED,
            ActorMarkerInventory.ATTACHMENT_KEY);

    public ActorMarkerBlockRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(ActorMarkerBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState state = entity.getCachedState();

        matrices.push();

        rotateToLocal(matrices, state);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlock(state, pos, world, matrices,
                vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, true)), false,
                Random.create(0));

        matrices.pop();

        renderItemAttachments(entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }

    public void renderItemAttachments(ActorMarkerBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
        renderAttachmentItem(
                HEAD_POSITION, renderer, entity, tickDelta, matrices,
                vertexConsumers,
                light, overlay);
        renderAttachmentItem(
                LEFT_HAND_POSITION, renderer, entity, tickDelta, matrices,
                vertexConsumers, light, overlay);
        renderAttachmentItem(RIGHT_HAND_POSITION, renderer, entity, tickDelta,
                matrices,
                vertexConsumers, light, overlay);
        renderAttachmentItem(
                ATTACHMENT_POSITION, renderer, entity, tickDelta,
                matrices,
                vertexConsumers, light, overlay);
    }

    private static void rotateToLocal(MatrixStack matrices, BlockState state) {
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y
                .rotationDegrees(ActorMarkerRotationHelper
                        .toDegrees(state.get(ActorMarkerBlock.ROTATION))));
        matrices.translate(-0.5, -0.5, -0.5);
    }

    public void renderAttachmentItem(ItemAttachmentPosition attachmentPosition, ItemRenderer renderer,
            ActorMarkerBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack item = entity.getItem(attachmentPosition.itemKey);
        if (!item.isEmpty()) {
            renderAttachmentItem(attachmentPosition, item, renderer, entity, tickDelta, matrices, vertexConsumers,
                    light, overlay);
        }
    }

    public void renderAttachmentItem(ItemAttachmentPosition attachmentPosition,
            ItemStack item, ItemRenderer renderer, ActorMarkerBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockState state = entity.getCachedState();
        matrices.push();

        rotateToLocal(matrices, state);
        attachmentPosition.transformToAttachment(matrices);
        if (attachmentPosition.isHand) {
            attachmentPosition.rotateToHand(matrices);
        }
        renderer.renderItem(null, item, attachmentPosition.mode, attachmentPosition.leftHanded, matrices,
                vertexConsumers, world, light, overlay, 0);

        matrices.pop();
    }
}