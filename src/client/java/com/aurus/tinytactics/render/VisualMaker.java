package com.aurus.tinytactics.render;

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
        
        long cx = Math.round(pos.getX());
        long cy = Math.round(pos.getY());
        long cz = Math.round(pos.getZ());

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
