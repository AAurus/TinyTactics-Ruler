package com.aurus.tinytactics.render;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;

public abstract class RenderUtils {

    public static final double RING_EDGE_FACTOR = 0.5;
    public static final int MIN_HALFRING_EDGE_COUNT = 3;

    public static void setRenderPreferences() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
    }

    public static void resetRenderPreferences() {
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    public static List<Vec3d> getRingAround(Vec3d normal, Vec3d centerPos, double diameter) {
        List<Vec3d> result = new ArrayList<>();
        Vec3d norm = normal.normalize();
        int ringEdgeCount = (MIN_HALFRING_EDGE_COUNT + (int) (diameter / RING_EDGE_FACTOR)) * 2;

        Vec3d sinNorm = getQuickPerpendicularNormal(norm);
        Vec3d cosNorm = norm.crossProduct(sinNorm);

        for (double i = 0; i < ringEdgeCount; i++) {
            double sinFac = Math.sin((i / ringEdgeCount) * (2 * Math.PI)) * diameter / 2;
            double cosFac = Math.cos((i / ringEdgeCount) * (2 * Math.PI)) * diameter / 2;
            Vec3d sinVec = sinNorm.multiply(sinFac);
            Vec3d cosVec = cosNorm.multiply(cosFac);
            result.add(centerPos.add(sinVec).add(cosVec));
        }

        return result;
    }

    public static Vec3d getQuickPerpendicularNormal(Vec3d vec) {
        if (vec.getX() != 0) {
            return new Vec3d(-vec.getY(), vec.getX(), 0).normalize();
        } else {
            return new Vec3d(0, vec.getZ(), -vec.getY()).normalize();
        }
    }
}
