package com.aurus.tinytactics.render;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

public class VisualManager {
    private static final List<RulerParticle> particles = new ArrayList<>();

    
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                for (RulerParticle particle : particles) {
                    if (particle.corner) {
                        VisualMaker.spawnCornerParticles(client.world, particle.pos, particle.particle);
                    } else {
                        VisualMaker.spawnCenterParticle(client.world, particle.pos, particle.particle);
                    }
                }
            }
        });
    }

    public static void renderLines(DimensionType dimension, MatrixStack matrices, BufferBuilderStorage bufferBuilders,
            Camera camera, Matrix4f projection) {
        // TODO Auto-generated method stub
    } 

    public static void renderLineGroup(BlockPos from, BlockPos to, Direction side) {
        //todo
    }

    public static void addParticle(boolean corner, BlockPos pos, ParticleEffect particle) {
        particles.add(new RulerParticle(corner, pos, particle));
    }

    public static void addParticle(boolean corner, Vec3d pos, ParticleEffect particle) {
        particles.add(new RulerParticle(corner, pos, particle));
    }

    public static void removeParticle(boolean corner, BlockPos pos) {
        particles.removeIf(particle -> particle.posEquals(pos) && particle.corner == corner);
    }

    public static void removeParticle(boolean corner, Vec3d pos) {
        particles.removeIf(particle -> particle.posEquals(pos) && particle.corner == corner);
    }

    public static void removeParticle(BlockPos pos) {
        particles.removeIf(particle -> particle.posEquals(pos));
    }

    public static void removeParticle(Vec3d pos) {
        particles.removeIf(particle -> particle.posEquals(pos));
    }

    public static void clearParticles() {
        particles.clear();
    }

    protected static class RulerParticle {
        private boolean corner;
        private Vec3d pos;
        private ParticleEffect particle;

        public RulerParticle(boolean corner, BlockPos pos, ParticleEffect particle) {
            this.corner = corner;
            this.pos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            this.particle = particle;
        }

        public RulerParticle(boolean corner, Vec3d pos, ParticleEffect particle) {
            this.corner = corner;
            this.pos = pos;
            this.particle = particle;
        }

        public boolean posEquals(BlockPos bPos) {
            Vec3i posRounded = new Vec3i((int) Math.round(pos.x), (int) Math.round(pos.y), (int) Math.round(pos.z));
            return bPos.equals(posRounded);
        }

        public boolean posEquals(Vec3d dPos) {
            Vec3i posRounded = new Vec3i((int) Math.round(pos.x), (int) Math.round(pos.y), (int) Math.round(pos.z));
            Vec3i dPosRounded = new Vec3i((int) Math.round(dPos.x), (int) Math.round(dPos.y), (int) Math.round(dPos.z));
            return dPosRounded.equals(posRounded);
        }
    }
}
