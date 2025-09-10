package com.aurus.tinytactics.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;

import com.aurus.tinytactics.data.TacticsRulerMap;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.DyeColor;

import java.awt.Color;

public class RenderManager {

    private TacticsRulerMap map;

    private static RenderManager manager;

    private static final double MAIN_RULER_LINE_OPACITY = 1.0;
    private static final float MAIN_RULER_LINE_WIDTH = 0.05F;

    private static final double CORNER_RULER_LINE_OPACITY = 0.5;
    private static final float CORNER_RULER_LINE_WIDTH = 0.02F;

    private RenderManager() {
        map = TacticsRulerMap.DEFAULT;
    }

    public static RenderManager getManager() {
        if (manager == null) {
            manager = new RenderManager();
        }
        return manager;
    }

    public void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            for (Map<DyeColor, List<BlockPos>> userMap : map.getFullMap().values()) {
                for (DyeColor color : userMap.keySet()) {

                    List<BlockPos> list = userMap.get(color);
                    List<Vec3d> vecs = blockPosToVec3ds(list);

                    if (vecs.size() >= 2) {
                        int mainColor = setColorAlpha(color.getEntityColor(), MAIN_RULER_LINE_OPACITY);
                        LineDrawer.renderQuadCrossLineStrip(context, vecs, mainColor, MAIN_RULER_LINE_WIDTH);

                        Vec3d from = vecs.get(Math.max(vecs.size() - 2, 0));
                        Vec3d to = vecs.get(Math.max(vecs.size() - 1, 0));
                        int conerColor = setColorAlpha(mainColor, CORNER_RULER_LINE_OPACITY);
                        LineDrawer.renderLinesToCorners(context, from, to, conerColor,
                                CORNER_RULER_LINE_WIDTH);
                    }

                }
            }

            ConeDrawer.renderDebugCone(context);
        });
    }

    public void updateMap(TacticsRulerMap map) {
        this.map = map;
    }

    public static List<Vec3d> blockPosToVec3ds(List<BlockPos> blockPos) {
        List<Vec3d> result = new ArrayList<>();
        for (BlockPos pos : blockPos) {
            result.add(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        }
        return result;
    }

    public static Vec3d blockPosToVec3d(BlockPos pos) {
        return new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static int setColorAlpha(int color, double alpha) {
        Color baseColor = new Color(color, true);
        Color newColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(),
                ((int) (alpha * 255)));
        return newColor.getRGB();

    }

    public static Quaternionf getQuaternionTo(Vec3d from, Vec3d to) {
        Vec3d xyz = from.crossProduct(to);
        float w = (float) (Math.sqrt((from.lengthSquared()) * (to.lengthSquared())) + from.dotProduct(to));
        return new Quaternionf((float) xyz.getX(), (float) xyz.getY(), (float) xyz.getZ(), w).normalize();
    }
}
