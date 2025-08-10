package com.aurus.tinytactics.render;

import java.util.Objects;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class LineDrawer {

    public static void renderLine(WorldRenderContext context, Vec3d pos1, Vec3d pos2, int color) {

        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());
        RenderManager.transformToWorldSpace(matrices, camera);

        final Tessellator tess = Tessellator.getInstance();

        BufferBuilder buffer = tess.begin(DrawMode.LINES, VertexFormats.LINES);

        Vec3d normal = pos1.subtract(pos2).normalize();
        float dx = (float) normal.getX();
        float dy = (float) normal.getY();
        float dz = (float) normal.getZ();

        buffer.vertex(matrices.peek(), (float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(color).normal(dx, dy, dz);
        buffer.vertex(matrices.peek(), (float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(color).normal(dx, dy, dz);

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public static void drawDebugLine(WorldRenderContext context) {
        renderLine(context, new Vec3d(0, 0, 0), new Vec3d(0, 40, 0), 0xFF0000FF);
    }
}
