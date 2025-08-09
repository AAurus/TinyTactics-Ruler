package com.aurus.tinytactics.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class LineDrawer {

    public static void renderLine(Vec3d pos1, Vec3d pos2, int color) {

        final Tessellator tess = Tessellator.getInstance();

        BufferBuilder buffer = tess.begin(DrawMode.LINES, VertexFormats.LINES);

        Vec3d normal = pos1.negate().add(pos2).normalize();
        float dx = (float) normal.getX();
        float dy = (float) normal.getY();
        float dz = (float) normal.getZ();

        buffer.vertex((float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(color).normal(dx, dy, dz);
        buffer.vertex((float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(color).normal(dx, dy, dz);

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public static void drawDebugLine(DimensionType dimension, MatrixStack matrices, BufferBuilderStorage bufferBuilders, Camera camera, Matrix4f projection) {
        //renderLine(dimension, matrices, bufferBuilders, camera, projection, new Vec3d(0, 0, 0), new Vec3d(0, 40, 0), 0xFF0000FF);
    }

    public static void renderLineNonTessellator(DimensionType dimension, MatrixStack matrices, BufferBuilderStorage bufferBuilders, Camera camera, Matrix4f projection, Vec3d pos1, Vec3d pos2) {
        Vec3d cameraPos = camera.getPos();

        matrices.push();

        VertexConsumer buffer = bufferBuilders.getOutlineVertexConsumers().getBuffer(LineRenderLayer.getRenderLayer());

        matrices.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

        buffer.vertex((float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.vertex((float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
    }
}
