package com.aurus.tinytactics.render;

import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import com.aurus.tinytactics.TinyTactics;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.util.Identifier;

public class TacticsDrawRenderPipelines {

    private static MappableRingBuffer vertexBuffer;

    public static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);

    public static final RenderPipeline TACTICS_LINES = RenderPipelines
            .register(setPreferencesAndBuild(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET),
                    VertexFormat.DrawMode.QUADS));

    public static final RenderPipeline TACTICS_CONES = RenderPipelines
            .register(setPreferencesAndBuild(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET),
                    VertexFormat.DrawMode.TRIANGLE_FAN));

    public static final RenderPipeline TACTICS_BANDS = RenderPipelines
            .register(setPreferencesAndBuild(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET),
                    VertexFormat.DrawMode.TRIANGLE_STRIP));

    private static RenderPipeline setPreferencesAndBuild(RenderPipeline.Builder builder,
            VertexFormat.DrawMode drawMode) {
        return builder.withLocation(Identifier.of(TinyTactics.MOD_ID, "pipeline/tactics_drawing"))
                .withVertexFormat(VertexFormats.POSITION_COLOR, drawMode)
                .withDepthTestFunction(DepthTestFunction.LESS_DEPTH_TEST)
                .withBlend(BlendFunction.TRANSLUCENT)
                .withCull(false)
                .build();
    }

    private TacticsDrawRenderPipelines() {
    }

    public static final BufferAllocator ALLOCATOR = new BufferAllocator(RenderLayer.CUTOUT_BUFFER_SIZE);

    public static GpuBuffer upload(BuiltBuffer.DrawParameters drawParameters, VertexFormat format,
            BuiltBuffer builtBuffer) {
        // Calculate the size needed for the vertex buffer
        int vertexBufferSize = drawParameters.vertexCount() * format.getVertexSize();

        // Initialize or resize the vertex buffer as needed
        if (vertexBuffer == null || vertexBuffer.size() < vertexBufferSize) {
            vertexBuffer = new MappableRingBuffer(() -> TinyTactics.MOD_ID + " Shape Drawing Pipeline",
                    GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE, vertexBufferSize);
        }

        // Copy vertex data into the vertex buffer
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

        try (GpuBuffer.MappedView mappedView = commandEncoder
                .mapBuffer(vertexBuffer.getBlocking().slice(0, builtBuffer.getBuffer().remaining()), false, true)) {
            MemoryUtil.memCopy(builtBuffer.getBuffer(), mappedView.data());
        }

        return vertexBuffer.getBlocking();
    }

    public static void rotateVertexBuffer() {
        vertexBuffer.rotate();
    }
}