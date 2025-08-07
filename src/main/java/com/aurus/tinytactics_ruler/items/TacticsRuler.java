package com.aurus.tinytactics_ruler.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TacticsRuler extends Item {
    protected static final Map<PlayerEntity, Measurement> MEASUREMENTS = new HashMap<>();
    public static List<Vec3d> activePos = new ArrayList<>();
    PlayerEntity player;

    public TacticsRuler(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        
        if (!context.getWorld.isClient()) {
            return ActionResult.PASS;
        }

        if (player.isSneaking()) {
            MEASUREMENTS.remove(player);
            return ActionResult.SUCCESS;
        }

        return measure(context);

    }

    @Environment(EnvType.CLIENT)
    private measure(ItemUsageContext context) {
        PlayerEntity usePlayer = context.getPlayer();

        Measurement data = MEASUREMENTS.computeIfAbsent(usePlayer, p -> new MeasurementData());

        BlockPos hitPos = context.getBlockPos();
        Direction hitSide = context.getSide();

        switch(data.step) {
            case 0:
            case 1:
                data.points[step] = hitPos;
                if (!data.world) {
                    data.world = context.getWorld();
                } else if (data.world != context.getWorld) {
                    // TODO: display message on player HUD
                    return ActionResult.FAIL;
                }

                if (data.points[1]) {
                    double dist = data.getDistance();
                }
                // TODO: display message on player HUD
                data.step++;

            break;
            default:
                
            break;
        }
    }

    protected static double euclidDistance(BlockPos a, BlockPos b) {
        int dx = b.getX() - a.getX();
        int dy = b.getY() - a.getY();
        int dz = b.getZ() - a.getZ();

        return Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2) + Math.pow(dz,2));
    }

    protected static class Measurement {
        public BlockPos[] points = new BlockPos[2];
        public World world;
        public int step = 0;
        public Measurement() {
            points[0] = null; //from
            points[1] = null; //to
        }

        public getDistance {
            if (points[0] && points[1]) {
                return euclidDistance(points[0], points[1]);
            }
            return 0;
        }
    }

}
