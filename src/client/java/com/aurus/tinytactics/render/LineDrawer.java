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

        MatrixStack.Entry matrix = matrices.peek();

        matrices.push();
        final Tessellator tess = Tessellator.getInstance();

        BufferBuilder buffer = tess.begin(DrawMode.DEBUG_LINES, VertexFormats.LINES);

        Vec3d normal = pos2.subtract(pos1).normalize();
        float dx = (float) normal.getX();
        float dy = (float) normal.getY();
        float dz = (float) normal.getZ();

        buffer.vertex(matrix.getPositionMatrix(), (float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(color).normal(matrix, dx, dy, dz);
        buffer.vertex(matrix.getPositionMatrix(), (float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(color).normal(matrix, dx, dy, dz);

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.lineWidth(2);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        matrices.pop();
    }

    public static void drawDebugLine(WorldRenderContext context) {
        renderLine(context, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0.5, 40.5, 0.5), 0xFF0000FF);
    }
}
