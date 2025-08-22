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

    public static void drawDebugLine(WorldRenderContext context) {
        renderQuadCrossLine(context, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0.5, 40.5, 40.5), 0xFF0000FF, 0.05);
    }

    public static void renderQuadCrossLine(WorldRenderContext context, Vec3d pos1, Vec3d pos2, int color,
            double width) {
        List<Vec3d> vecs = createQuadCross(pos1, pos2, width);
        renderQuadsInWorld(context, vecs, color);
    }

    public static void renderQuadCrossLineStrip(WorldRenderContext context, List<Vec3d> positions, int color,
            double width) {
        List<Vec3d> vecs = new ArrayList<>();
        for (int i = 1; i < positions.size(); i++) {
            Vec3d pos1 = positions.get(i - 1);
            Vec3d pos2 = positions.get(i);
            vecs.addAll(createQuadCross(pos1, pos2, width));
        }
        renderQuadsInWorld(context, vecs, color);
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

    public static void renderQuadsInWorld(WorldRenderContext context, List<Vec3d> finalVecs, int color) {
        if (!finalVecs.isEmpty()) {
            Camera camera = context.camera();
            MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

            matrices.push();

            final Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

            for (Vec3d vec : finalVecs) {
                // Vec3d camVec = RenderManager.transformToCameraSpace(vec, camera);
                buffer.vertex(matrices.peek(), (float) vec.getX(), (float) vec.getY(), (float) vec.getZ()).color(color);
            }

            // RenderSystem.enableBlend();
            // RenderSystem.defaultBlendFunc();
            // RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            matrices.pop();
        }
    }

}
