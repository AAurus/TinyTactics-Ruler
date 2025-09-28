package com.aurus.tinytactics.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
//import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class LineDrawer {

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

        Vec3d norm1 = RenderUtils.getQuickPerpendicularNormal(norm);
        Vec3d norm2 = norm.crossProduct(norm1);

        Vec3d scale1 = norm1.multiply(lineWidth);
        Vec3d scale2 = norm2.multiply(lineWidth);

        result.add(pos1.add(scale1));
        result.add(pos2.add(scale1));
        result.add(pos2.subtract(scale1));
        result.add(pos1.subtract(scale1));
        result.add(pos2.add(scale2));
        result.add(pos1.add(scale2));
        result.add(pos1.subtract(scale2));
        result.add(pos2.subtract(scale2));

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
                buffer.vertex(matrices.peek(), (float) vec.getX(), (float) vec.getY(), (float) vec.getZ()).color(color);
            }

            RenderUtils.setRenderPreferences();

            //BufferRenderer.drawWithGlobalProgram(buffer.end());

            matrices.pop();
        }
    }

}
