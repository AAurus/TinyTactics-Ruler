package com.aurus.tinytactics.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class ConeDrawer {

    public static final double RING_EDGE_FACTOR = 0.5;
    public static final int MIN_HALFRING_EDGE_COUNT = 3;

    public static void renderDebugCone(WorldRenderContext context) {
        renderCone(context, new Vec3d(0.5, 2.5, 0.5), 10, 3, new Vec3d(1, 0, 0), 0x440000FF);
    }

    public static void renderCone(WorldRenderContext context, Vec3d tipPos, double length, double diameter,
            Vec3d normal, int color) {
        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        Vec3d endPos = tipPos.add(normal.normalize().multiply(length));
        List<Vec3d> ring = getRingAround(normal, endPos, diameter);

        renderTip(camera, matrices, tipPos, ring, diameter, color);
        renderBase(camera, matrices, ring, diameter, color);
    }

    public static void renderTip(Camera camera, MatrixStack matrices, Vec3d tipPos, List<Vec3d> baseRing,
            double width, int color) {
        matrices.push();

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        buffer.vertex(matrices.peek(), (float) tipPos.getX(), (float) tipPos.getY(),
                (float) tipPos.getZ()).color(color);

        for (Vec3d vec : baseRing) {
            buffer.vertex(matrices.peek(), (float) vec.getX(), (float) vec.getY(), (float) vec.getZ()).color(color);
        }

        Vec3d finalVec = baseRing.get(0);
        buffer.vertex(matrices.peek(), (float) finalVec.getX(), (float) finalVec.getY(), (float) finalVec.getZ())
                .color(color);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();
    }

    public static void renderBase(Camera camera, MatrixStack matrices, List<Vec3d> baseRing,
            double width, int color) {
        matrices.push();

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        for (Vec3d vec : baseRing) {
            buffer.vertex(matrices.peek(), (float) vec.getX(), (float) vec.getY(), (float) vec.getZ()).color(color);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();
    }

    public static List<Vec3d> getRingAround(Vec3d normal, Vec3d centerPos, double diameter) {
        List<Vec3d> result = new ArrayList<>();
        Vec3d norm = normal.normalize();
        int ringEdgeCount = (MIN_HALFRING_EDGE_COUNT + (int) (diameter / RING_EDGE_FACTOR)) * 2;

        Vec3d normXY, normYZ = Vec3d.ZERO;
        if (norm.getX() != 0) {
            normXY = new Vec3d(-norm.getY(), norm.getX(), 0).normalize();
            normYZ = norm.crossProduct(normXY);
        } else {
            normYZ = new Vec3d(0, norm.getZ(), -norm.getY()).normalize();
            normXY = norm.crossProduct(normYZ);
        }

        for (double i = 0; i < ringEdgeCount; i++) {
            double xFac = Math.sin((i / ringEdgeCount) * (2 * Math.PI)) * diameter / 2;
            double zFac = Math.cos((i / ringEdgeCount) * (2 * Math.PI)) * diameter / 2;
            Vec3d xVec = normXY.multiply(xFac);
            Vec3d zVec = normYZ.multiply(zFac);
            result.add(centerPos.add(xVec).add(zVec));
        }

        return result;
    }
}
