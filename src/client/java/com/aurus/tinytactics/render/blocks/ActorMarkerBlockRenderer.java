package com.aurus.tinytactics.render.blocks;

import org.joml.Vector3f;

import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlock;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlockEntity;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerRotationHelper;

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
            new EulerAngle(0, 0, 0), ModelTransformationMode.HEAD);
    private static final ItemAttachmentPosition LEFT_HAND_POSITION = new ItemAttachmentPosition(
            new Vector3f(5.0F, 4.0F, 6.5F),
            new Vector3f(0.5F,
                    0.5F,
                    0.5F),
            new EulerAngle(40, 20, -90), ModelTransformationMode.THIRD_PERSON_LEFT_HAND, true, true);
    private static final ItemAttachmentPosition RIGHT_HAND_POSITION = new ItemAttachmentPosition(
            new Vector3f(11.0F, 4.0F, 6.5F),
            new Vector3f(0.5F,
                    0.5F,
                    0.5F),
            new EulerAngle(40, -20, 90), ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, true);
    private static final ItemAttachmentPosition ATTACHMENT_POSITION = new ItemAttachmentPosition(
            new Vector3f(8.0F, 7.5F, 12F),
            new Vector3f(0.4F,
                    0.4F,
                    0.4F),
            new EulerAngle(0, 180, 0), ModelTransformationMode.FIXED);

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
        HEAD_POSITION.renderAttachmentItem(entity.getHeadItem(), renderer, entity, tickDelta, matrices,
                vertexConsumers,
                light, overlay);
        LEFT_HAND_POSITION.renderAttachmentItem(entity.getLeftHandItem(), renderer, entity, tickDelta, matrices,
                vertexConsumers, light, overlay);
        RIGHT_HAND_POSITION.renderAttachmentItem(entity.getRightHandItem(), renderer, entity, tickDelta,
                matrices,
                vertexConsumers, light, overlay);
        ATTACHMENT_POSITION.renderAttachmentItem(entity.getAttachmentItem(), renderer, entity, tickDelta,
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

    public static class ItemAttachmentPosition {
        public Vector3f position;
        public Vector3f scale;
        public EulerAngle rotation;
        public ModelTransformationMode mode;
        public boolean leftHanded = false;
        public boolean isHand = false;

        public ItemAttachmentPosition(Vector3f position, Vector3f scale, EulerAngle rotation,
                ModelTransformationMode mode) {
            this.position = position;
            this.scale = scale;
            this.rotation = rotation;
            this.mode = mode;
        }

        public ItemAttachmentPosition(Vector3f position, Vector3f scale, EulerAngle rotation,
                ModelTransformationMode mode, boolean isHand) {
            this.position = position;
            this.scale = scale;
            this.rotation = rotation;
            this.mode = mode;
            this.isHand = isHand;
        }

        public ItemAttachmentPosition(Vector3f position, Vector3f scale, EulerAngle rotation,
                ModelTransformationMode mode, boolean isHand, boolean leftHanded) {
            this.position = position;
            this.scale = scale;
            this.rotation = rotation;
            this.mode = mode;
            this.isHand = isHand;
            this.leftHanded = leftHanded;
        }

        public void renderAttachmentItem(ItemStack item, ItemRenderer renderer, ActorMarkerBlockEntity entity,
                float tickDelta,
                MatrixStack matrices,
                VertexConsumerProvider vertexConsumers, int light, int overlay) {
            World world = entity.getWorld();
            BlockState state = entity.getCachedState();
            matrices.push();
            rotateToLocal(matrices, state);
            transformToAttachment(matrices);
            if (isHand) {
                rotateToHand(matrices);
            }
            renderer.renderItem(null, item, mode, leftHanded, matrices, vertexConsumers, world, light, overlay, 0);
            matrices.pop();
        }

        public void transformToAttachment(MatrixStack matrices) {
            float[] pos = { position.x() / 16, position.y() / 16, position.z() / 16 };

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation.getWrappedYaw()),
                    pos[0], pos[1], pos[2]);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation.getWrappedPitch()),
                    pos[0], pos[1], pos[2]);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation.getWrappedRoll()),
                    pos[0], pos[1], pos[2]);

            matrices.translate(pos[0], pos[1], pos[2]);

            matrices.scale(scale.x(), scale.y(), scale.z());
        }

        public void rotateToHand(MatrixStack matrices) {
            int handedness = this.leftHanded ? 1 : -1;
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handedness * 45));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(handedness * 70));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(handedness * 120.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(200.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(handedness * -135.0F));
        }
    }
}