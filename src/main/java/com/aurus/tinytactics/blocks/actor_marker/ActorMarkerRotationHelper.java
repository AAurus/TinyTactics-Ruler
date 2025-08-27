package com.aurus.tinytactics.blocks.actor_marker;

import net.minecraft.util.math.RotationCalculator;
import net.minecraft.util.math.RotationPropertyHelper;

public class ActorMarkerRotationHelper extends RotationPropertyHelper {

    private static final RotationCalculator LOCAL_CALCULATOR = new RotationCalculator(2);
    public static final int FRONT = -2;
    public static final int LEFT = -3;
    public static final int BACK = -4;
    public static final int RIGHT = -5;

    public static int toLocalDirection(float yaw, int rotation) {
        float localYaw = yaw - toDegrees(rotation) % 360;
        if (yaw >= 180) {
            yaw -= 360;
        } else if (yaw <= -180) {
            yaw += 360;
        }
        return LOCAL_CALCULATOR.toClampedRotation(localYaw) - 2;
    }

}
