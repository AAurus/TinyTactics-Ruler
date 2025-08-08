package com.aurus.tinytactics.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.util.math.Vec3d;

public class LineDrawer {
    public static void renderLine(Vec3d pos1, Vec3d pos2, int color) {

        Tessellator tess = Tessellator.getInstance();

        BufferBuilder buffer = tess.begin(DrawMode.LINES, VertexFormats.LINES);

        buffer.vertex((float) pos1.getX(), (float) pos1.getY(), (float) pos1.getZ()).color(color);
        buffer.vertex((float) pos2.getX(), (float) pos2.getY(), (float) pos2.getZ()).color(color);

        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public static void drawDebugLine() {
        renderLine(new Vec3d(0, 0, 0), new Vec3d(0, 40, 0), 0xFF0000FF);
    }
}
