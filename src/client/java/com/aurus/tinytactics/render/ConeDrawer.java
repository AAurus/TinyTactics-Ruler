package com.aurus.tinytactics.render;

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

    public static void renderDebugCone(WorldRenderContext context) {
        renderCone(context, new Vec3d(0.5, 2.5, 0.5), 10, 3, new Vec3d(1, 1, 1), 0x440000FF);
    }

    public static void renderCone(WorldRenderContext context, Vec3d tipPos, double length, double diameter,
            Vec3d normal, int color) {
        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        Vec3d endPos = tipPos.add(normal.normalize().multiply(length));
        List<Vec3d> ring = RenderUtils.getRingAround(normal, endPos, diameter);

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

        RenderUtils.setRenderPreferences();

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

        RenderUtils.setRenderPreferences();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrices.pop();
    }
}
