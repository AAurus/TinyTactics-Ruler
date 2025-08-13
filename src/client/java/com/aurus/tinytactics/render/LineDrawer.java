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
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class LineDrawer {

    public static void renderLine(WorldRenderContext context, Vec3d pos1, Vec3d pos2, int color) {

        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        matrices.push();

        MatrixStack.Entry matrix = matrices.peek();

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.LINES);

        Vec3d normal = pos2.subtract(pos1).normalize();
        float dx = (float) normal.getX();
        float dy = (float) normal.getY();
        float dz = (float) normal.getZ();

        Vec3d cam1 = RenderManager.transformToCameraSpace(pos1, camera);
        Vec3d cam2 = RenderManager.transformToCameraSpace(pos2, camera);

        buffer.vertex(matrix.getPositionMatrix(), (float) cam1.getX(), (float) cam1.getY(), (float) cam1.getZ()).color(color).normal(matrix, dx, dy, dz);
        buffer.vertex(matrix.getPositionMatrix(), (float) cam2.getX(), (float) cam2.getY(), (float) cam2.getZ()).color(color).normal(matrix, dx, dy, dz);

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.lineWidth(2);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        
        matrices.pop();
    }

    public static void renderQuadCrossLine(WorldRenderContext context, Vec3d pos1, Vec3d pos2, int color) {
        Camera camera = context.camera();
        MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

        matrices.push();

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

        List<Vec3d> vecs = createQuadCross(pos1, pos2, 0.05);
        for (Vec3d vec : vecs) {
            //Vec3d camVec = RenderManager.transformToCameraSpace(vec, camera);
            buffer.vertex(matrices.peek(), (float) vec.getX(), (float) vec.getY(), (float) vec.getZ()).color(color);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        
        matrices.pop();
    }

    public static List<Vec3d> createQuadCross(Vec3d pos1, Vec3d pos2, double lineWidth) {
        List<Vec3d> result = new ArrayList<>();
        
        Vec3d diff = pos2.subtract(pos1);
        Vec3d norm = diff.normalize();

        Vec3d normXY = new Vec3d(-norm.getY(), norm.getX(), 0).normalize();
        Vec3d normYZ = new Vec3d(0, -norm.getZ(), norm.getY()).normalize();
        
        Vec3d scaleXY = normXY.multiply(lineWidth);
        Vec3d scaleYZ = normYZ.multiply(lineWidth);

        result.add(pos1.add(scaleXY));
        result.add(pos2.add(scaleXY));
        result.add(pos2.subtract(scaleXY));
        result.add(pos1.subtract(scaleXY));
        result.add(pos2.add(scaleYZ));
        result.add(pos1.add(scaleYZ));
        result.add(pos1.subtract(scaleYZ));
        result.add(pos2.subtract(scaleYZ));

        return result;
    }

    public static void drawDebugLine(WorldRenderContext context) {
        renderQuadCrossLine(context, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0.5, 40.5, 40.5), 0xFF0000FF);
    }
}
