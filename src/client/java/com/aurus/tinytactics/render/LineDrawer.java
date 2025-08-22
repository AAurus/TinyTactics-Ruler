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
        List<Vec3d> vec3ds = new ArrayList<>();
        vec3ds.add(new Vec3d(0.5, 0.5, 0.5));
        vec3ds.add(new Vec3d(0.5, 40.5, 40.5));
        renderLinesInWorld(context, vec3ds,
                0xFF0000FF, 5F);
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

    public static void renderLinesToCorners(WorldRenderContext context, Vec3d from, Vec3d to, int color,
            float width) {
        List<Vec3d> vecs = new ArrayList<>();
        vecs.add(to.add(0.5, 0.5, 0.5));
        vecs.add(to.add(0.5, 0.5, -0.5));
        vecs.add(to.add(0.5, -0.5, 0.5));
        vecs.add(to.add(0.5, -0.5, -0.5));
        vecs.add(to.add(-0.5, 0.5, 0.5));
        vecs.add(to.add(-0.5, 0.5, -0.5));
        vecs.add(to.add(-0.5, -0.5, 0.5));
        vecs.add(to.add(-0.5, -0.5, -0.5));
        for (Vec3d vec : vecs) {
            renderQuadCrossLine(context, from, vec, color, width);
        }
    }

    public static List<Vec3d> createQuadCross(Vec3d pos1, Vec3d pos2, double lineWidth) {
        List<Vec3d> result = new ArrayList<>();

        Vec3d diff = pos2.subtract(pos1);
        Vec3d norm = diff.normalize();

        Vec3d normXY = new Vec3d(-norm.getY(), norm.getX(), 0).normalize();
        Vec3d normYZ = norm.crossProduct(normXY);

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

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            // RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            // RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            matrices.pop();
        }
    }

    public static void renderLinesInWorld(WorldRenderContext context, List<Vec3d> vecs, int color, float width) {
        if (!vecs.isEmpty()) {
            Camera camera = context.camera();
            MatrixStack matrices = Objects.requireNonNull(context.matrixStack());

            matrices.push();

            final Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.begin(DrawMode.LINE_STRIP, VertexFormats.LINES);

            matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);

            Vec3d normal = null;
            Vec3d sum = Vec3d.ZERO;

            for (int i = 1; i < vecs.size(); i++) {
                Vec3d to = vecs.get(i);
                Vec3d from = vecs.get(i - 1);
                normal = to.subtract(from).normalize();
                buffer.vertex(matrices.peek(), (float) from.getX(), (float) from.getY(), (float) from.getZ())
                        .color(color)
                        .normal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());
                sum = sum.add(from);
            }
            Vec3d finalVec = vecs.get(vecs.size() - 1);
            buffer.vertex(matrices.peek(), (float) finalVec.getX(), (float) finalVec.getY(), (float) finalVec.getZ())
                    .color(color)
                    .normal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());
            sum = sum.add(finalVec);

            double distance = sum.multiply((double) (1 / vecs.size())).subtract(camera.getPos()).length();

            // RenderSystem.enableBlend();
            // RenderSystem.defaultBlendFunc();
            // RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.lineWidth(width / ((float) distance));
            RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
            // RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            BufferRenderer.drawWithGlobalProgram(buffer.end());

            matrices.pop();
        }
    }

}
