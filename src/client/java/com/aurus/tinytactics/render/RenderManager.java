package com.aurus.tinytactics.render;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

public class RenderManager {
    
    public static Vec3d transformToCameraSpace(Vec3d pos, Camera camera) {
        //Vec3d tempT = pos.add(camera.getPos());
        //Vector3d tempTR = camera.getRotation().transform(new Vector3d(tempT.getX(), tempT.getY(), tempT.getZ()));
        //return new Vec3d(tempTR.x(), tempTR.y(), tempTR.z());
        return pos.subtract(camera.getPos());
    }
}
