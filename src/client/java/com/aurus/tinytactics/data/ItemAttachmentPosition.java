package com.aurus.tinytactics.data;

import org.joml.Vector3f;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.RotationAxis;

public class ItemAttachmentPosition {
    public Vector3f position;
    public Vector3f scale;
    public EulerAngle rotation;
    public ModelTransformationMode mode;
    public boolean leftHanded = false;
    public boolean isHand = false;
    public String itemKey;

    public ItemAttachmentPosition(Vector3f position, Vector3f scale, EulerAngle rotation,
            ModelTransformationMode mode, String itemKey) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.mode = mode;
        this.itemKey = itemKey;
    }

    public ItemAttachmentPosition(Vector3f position, Vector3f scale, EulerAngle rotation,
            ModelTransformationMode mode, String itemKey, boolean isHand) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.mode = mode;
        this.itemKey = itemKey;
        this.isHand = isHand;
    }

    public ItemAttachmentPosition(Vector3f position, Vector3f scale, EulerAngle rotation,
            ModelTransformationMode mode, String itemKey, boolean isHand, boolean leftHanded) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.mode = mode;
        this.itemKey = itemKey;
        this.isHand = isHand;
        this.leftHanded = leftHanded;
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
