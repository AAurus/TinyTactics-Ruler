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
            clearPoints();
            return ActionResult.SUCCESS;
        }

        return measure(context);
    }

    @Environment(EnvType.CLIENT)
    private measure(ItemUsageContext context) {
        
        PlayerEntity usePlayer = context.getPlayer();
        Measurement data = MEASUREMENTS.computeIfAbsent(usePlayer, p -> new MeasurementData());

        if (data.step < 2) {
            if (!checkWorld(data, context.getWorld())) {
                return ActionResult.FAIL;
            }

            if (!data.points[step]) {
                BlockPos pos = context.getBlockPos()
                data.points[step] = pos;
                VisualManager.addParticle(true, pos, ParticleTypes.END_ROD); // debug
            }
            
            if (step == 1) {
                double dist = data.getDistance();
                // TODO: display message on player HUD
                VisualManager.renderLineGroup(data.points[0], data.points[1], context.getSide());
            }
            
            data.step++;
        } 
        else {
            MEASUREMENTS.remove(player);
            clearPoints();
        }

        return ActionResult.SUCCESS;
    }

    private boolean checkWorld(Measurement data, World world) {
        
        if (!data.world) {
            data.world = world;
        }
        else if (data.world != world) {
            // TODO: display message on player HUD
            return false;
        }

        return true;
    }

    protected void clearPoints() {
        
        if (!this.player) {
            return;
        }

        Measurement data = MEASUREMENTS.get(player);
        if (data) {
            VisualManager.removeParticle(data.points[0]);
            VisualManager.removeParticle(data.points[1]);
        }
    }

    protected static class Measurement {
        public BlockPos[] points = new BlockPos[2];
        public World world;
        public int step = 0;
        public Measurement() {
            points[0] = null; //from
            points[1] = null; //to
        }
        
        protected static double euclidDistance(BlockPos a, BlockPos b) {
            
            int dx = b.getX() - a.getX();
            int dy = b.getY() - a.getY();
            int dz = b.getZ() - a.getZ();

            return Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2) + Math.pow(dz,2));
        }

        public getDistance {
            
            if (points[0] && points[1]) {
                return euclidDistance(points[0], points[1]);
            }

            return 0;
        }
    }
}
