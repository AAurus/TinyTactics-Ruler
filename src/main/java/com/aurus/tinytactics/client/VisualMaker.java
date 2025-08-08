package com.aurus.tinytactics.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

// import net.minecraft.client.render.Tessellator;
// import net.minecraft.client.render.BufferBuilder;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class VisualMaker {
    public static void renderLineGroup(BlockPos from, BlockPos to) {
        //todo
    }

    @Environment(EnvType.CLIENT)
    private static void spawnStaticParticle(World world, ParticleEffect particle, double x, double y, double z) {
        
        if (world.isClient) {
            world.addParticle(particle, x, y, z, 0, 0, 0);
        }

    }

    @Environment(EnvType.CLIENT)
    public static void spawnCenterParticle(World world, Vec3d pos, ParticleEffect particle) {
        spawnStaticParticle(world, particle, pos.getX(), pos.getY(), pos.getZ());
    }

    @Environment(EnvType.CLIENT)
    public static void spawnCornerParticles(World world, Vec3d pos, ParticleEffect particle) {
        
        double cx = Math.round(pos.getX()) + 0.5;
        double cy = Math.round(pos.getY()) + 0.5;
        double cz = Math.round(pos.getZ()) + 0.5;

        spawnStaticParticle(world, particle, cx+0.5, cy+0.5, cz+0.5);
        spawnStaticParticle(world, particle, cx-0.5, cy+0.5, cz+0.5);
        spawnStaticParticle(world, particle, cx+0.5, cy-0.5, cz+0.5);
        spawnStaticParticle(world, particle, cx-0.5, cy-0.5, cz+0.5);
        spawnStaticParticle(world, particle, cx+0.5, cy+0.5, cz-0.5);
        spawnStaticParticle(world, particle, cx-0.5, cy+0.5, cz-0.5);
        spawnStaticParticle(world, particle, cx+0.5, cy-0.5, cz-0.5);
        spawnStaticParticle(world, particle, cx-0.5, cy-0.5, cz-0.5); // is there a more elegant way to do this?
        
    }
}
