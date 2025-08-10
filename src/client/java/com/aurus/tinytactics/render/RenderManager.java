package com.aurus.tinytactics.render;

import org.joml.Quaternionf;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class RenderManager {
    
    public static void transformToWorldSpace(MatrixStack matrices, Camera camera) {
        matrices.multiply(camera.getRotation().conjugate(new Quaternionf()));
        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
    }
}
