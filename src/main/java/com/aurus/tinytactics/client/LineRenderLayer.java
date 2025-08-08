package com.aurus.tinytactics.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;

public class LineRenderLayer extends RenderLayer {
    public LineRenderLayer(String name, VertexFormat vertexFormat, DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getRenderLayer() {
        return of("lines_no_depth", VertexFormats.POSITION_COLOR, DrawMode.LINES, 256, 
            RenderLayer.MultiPhaseParameters.builder()
                .lineWidth(FULL_LINE_WIDTH)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .texture(NO_TEXTURE)
                .cull(DISABLE_CULLING)
                .depthTest(ALWAYS_DEPTH_TEST)
                .build(false));
    }
}
