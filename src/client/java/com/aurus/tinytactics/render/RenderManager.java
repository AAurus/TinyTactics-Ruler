package com.aurus.tinytactics.render;

import java.util.ArrayList;
import java.util.List;

import com.aurus.tinytactics.components.BlockPosList;
import com.aurus.tinytactics.components.BlockPosMap;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RenderManager {
    
    private BlockPosMap map;

    private static RenderManager manager;

    private RenderManager() {
        map = BlockPosMap.DEFAULT;
    }

    public static RenderManager getManager() {
        if (manager == null) {
            manager = new RenderManager();
        }
        return manager;
    }

    public void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            for (BlockPosList list : map.getPosmap().values()) {
                List<Vec3d> vecs = blockPosToVec3ds(list.getPositions());
                LineDrawer.renderQuadCrossLineStrip(context, vecs, 0xFFFFFFFF, 0.05);
            }
        });
    }

    public void updateMap(BlockPosMap map) {
        this.map = map;
    }

    public static Vec3d transformToCameraSpace(Vec3d pos, Camera camera) {
        //Vec3d tempT = pos.add(camera.getPos());
        //Vector3d tempTR = camera.getRotation().transform(new Vector3d(tempT.getX(), tempT.getY(), tempT.getZ()));
        //return new Vec3d(tempTR.x(), tempTR.y(), tempTR.z());
        return pos.subtract(camera.getPos());
    }

    public static List<Vec3d> blockPosToVec3ds(List<BlockPos> blockPos) {
        List<Vec3d> result = new ArrayList<>();
        for (BlockPos pos : blockPos) {
            result.add(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        }
        return result;
    }
}
