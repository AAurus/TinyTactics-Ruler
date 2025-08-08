package com.aurus.tinytactics.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

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

        LineDrawer.drawDebugLine();
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
            return (bPos.getX() == (int) Math.round(pos.x) && 
                    bPos.getY() == (int) Math.round(pos.y) && 
                    bPos.getZ() == (int) Math.round(pos.z));
        }

        public boolean posEquals(Vec3d dPos) {
            return (Math.round(pos.x) == Math.round(dPos.x) &&
                    Math.round(pos.y) == Math.round(dPos.y) &&
                    Math.round(pos.z) == Math.round(dPos.z));
        }
    }
}
