package com.aurus.tinytactics.render;

import java.util.List;
import java.util.Objects;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class ShapeDrawer {

    public static void renderCone(WorldRenderContext context, Vec3d tipPos, double length, double diameter,
            Vec3d normal, int color) {
        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        if (diameter <= 0) {
            diameter = RenderUtils.MIN_DIAMETER;
        }

        Vec3d endPos = tipPos.add(normal.normalize().multiply(length));
        List<Vec3d> ring = RenderUtils.getRingAround(normal, endPos, diameter);

        renderTip(camera, matrices, tipPos, ring, color);
        renderBase(camera, matrices, ring, color);
    }

    public static void renderCylinder(WorldRenderContext context, Vec3d basePos, double length, double diameter,
            Vec3d normal, int color) {
        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        if (diameter <= 0) {
            diameter = RenderUtils.MIN_DIAMETER;
        }

        Vec3d endPos = basePos.add(normal.normalize().multiply(length));
        List<Vec3d> baseRing = RenderUtils.getRingAround(normal, basePos, diameter);
        List<Vec3d> endRing = RenderUtils.getRingAround(normal, endPos, diameter);

        renderBase(camera, matrices, baseRing, color);
        renderBand(camera, matrices, baseRing, endRing, color);
        renderBase(camera, matrices, endRing, color);
    }

    public static void renderSphere(WorldRenderContext context, Vec3d centerPos, double diameter, int color) {

        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        if (diameter <= 0) {
            diameter = RenderUtils.MIN_DIAMETER;
        }

        Vec3d normal = new Vec3d(0, 1, 0);

        List<Vec2f> sphereData = RenderUtils.getSphereData(diameter);
        int sphereSegments = sphereData.size() * 2;

        List<Vec3d> prevRing = RenderUtils.getRingAround(normal, centerPos.add(0, sphereData.get(0).y, 0),
                sphereData.get(0).x, sphereSegments);

        ShapeDrawer.renderTip(camera, matrices, centerPos.add(0, diameter / 2, 0),
                prevRing, color);

        for (int i = 1; i < sphereData.size(); i++) {
            Vec3d currentRingCenter = centerPos.add(0, sphereData.get(i).y, 0);
            List<Vec3d> currentRing = RenderUtils.getRingAround(normal, currentRingCenter, sphereData.get(i).x,
                    sphereSegments);
            renderBand(camera, matrices, prevRing, currentRing, color);
            prevRing = currentRing;
        }

        ShapeDrawer.renderTip(camera, matrices, centerPos.add(0, -diameter / 2, 0),
                prevRing, color);

    }

    public static void renderTip(Camera camera, MatrixStack matrices, Vec3d tipPos, List<Vec3d> baseRing, int color) {
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

        RenderUtils.setRenderPreferences();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();
    }

    public static void renderBase(Camera camera, MatrixStack matrices, List<Vec3d> baseRing, int color) {
        matrices.push();

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        for (Vec3d vec : baseRing) {
            buffer.vertex(matrices.peek(), (float) vec.getX(), (float) vec.getY(), (float) vec.getZ()).color(color);
        }

        RenderUtils.setRenderPreferences();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();
    }

    public static void renderBand(Camera camera, MatrixStack matrices, List<Vec3d> ring1, List<Vec3d> ring2,
            int color) {
        matrices.push();

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        for (int i = 0; i < ring1.size(); i++) {
            Vec3d vec1 = ring1.get(i);
            Vec3d vec2 = ring2.get(i);
            buffer.vertex(matrices.peek(), (float) vec1.getX(), (float) vec1.getY(), (float) vec1.getZ()).color(color);
            buffer.vertex(matrices.peek(), (float) vec2.getX(), (float) vec2.getY(), (float) vec2.getZ()).color(color);
        }

        Vec3d finalVec1 = ring1.get(0);
        Vec3d finalVec2 = ring2.get(0);
        buffer.vertex(matrices.peek(), (float) finalVec1.getX(), (float) finalVec1.getY(), (float) finalVec1.getZ())
                .color(color);
        buffer.vertex(matrices.peek(), (float) finalVec2.getX(), (float) finalVec2.getY(), (float) finalVec2.getZ())
                .color(color);

        RenderUtils.setRenderPreferences();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();
    }
}
