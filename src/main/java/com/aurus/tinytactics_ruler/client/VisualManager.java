package com.aurus.tinytactics_ruler.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;

public static class VisualManager {
    private static final List<RulerParticle> particles = new ArrayList<>;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world) {
                for (particle : particles) {
                    if (particle.corner) {
                        VisualMaker.spawnCornerParticles(client.world, particle.pos, particle.particle);
                    } else {
                        VisualMaker.spawnCenterParticle(client.world, particle.pos, particle.particle);
                    }
                }
            }
        });
    }

    public static void renderLineGroup(BlockPos from, BlockPos to) {
        //todo
    }

    public static void addParticle(boolean corner, BlockPos pos, ParticleEffect particle) {
        particles.add(new RulerParticle(corner, pos, particle));
    }

    public static void addParticle(boolean corner, Vec3d pos, ParticleEffect particle) {
        particles.add(new RulerParticle(corner, pos, particle));
    }

    public static void removeParticle(boolean corner, BlockPos pos) {
        particles.removeIf(particle -> particle.pos.equals(pos) && particle.corner = corner);
    }

    public static void removeParticle(boolean corner, Vec3d pos) {
        particles.removeIf(particle -> particle.pos.equals(pos) && particle.corner = corner);
    }

    public static void removeParticle(BlockPos pos) {
        particles.removeIf(particle -> particle.pos.equals(pos));
    }

    public static void removeParticle(Vec3d pos) {
        particles.removeIf(particle -> particle.pos.equals(pos));
    }

    public static void clearParticles() {
        particles.clear();
    }

    protected static class RulerParticle {
        private boolean corner;
        private BlockPos pos;
        private ParticleEffect particle;

        public RulerParticle(boolean corner, BlockPos pos, ParticleEffect particle) {
            this.corner = corner;
            this.pos = pos;
            this.particle = particle;
        }

        public RulerParticle(boolean corner, Vec3d pos, ParticleEffect particle) {
            this.corner = corner;
            this.pos = new BlockPos(pos);
            this.particle = particle;
        }
    }
}
