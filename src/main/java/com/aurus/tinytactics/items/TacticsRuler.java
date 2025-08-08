package com.aurus.tinytactics.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import com.aurus.tinytactics.client.VisualManager;

public class TacticsRuler extends Item {
    protected static final Map<PlayerEntity, Measurement> MEASUREMENTS = new HashMap<>();
    PlayerEntity player;

    public TacticsRuler() {
        super(new Item.Settings().maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        this.player = context.getPlayer();

        if (!context.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        if (player.isSneaking()) {
            clearPoints();
            MEASUREMENTS.remove(player);
            return ActionResult.SUCCESS;
        }

        return measure(context);
    }

    @Environment(EnvType.CLIENT)
    private ActionResult measure(ItemUsageContext context) {
        
        PlayerEntity usePlayer = context.getPlayer();
        Measurement data = MEASUREMENTS.computeIfAbsent(usePlayer, p -> new Measurement());

        if (data.step < 2) {
            if (!checkWorld(data, context.getWorld())) {
                return ActionResult.FAIL;
            }

            if (data.points[data.step] == null) {
                BlockPos pos = context.getBlockPos();
                data.points[data.step] = pos;
                VisualManager.addParticle(true, pos, ParticleTypes.END_ROD); // debug
            }
            
            if (data.step == 1) {
                // double dist = data.getDistance();
                // TODO: display message on player HUD
                VisualManager.renderLineGroup(data.points[0], data.points[1], context.getSide());
            }
            
            data.step++;
        } 
        else {
            clearPoints();
            MEASUREMENTS.remove(player);
        }

        return ActionResult.SUCCESS;
    }

    private boolean checkWorld(Measurement data, World world) {
        
        if (data.world == null) {
            data.world = world;
        }
        else if (data.world != world) {
            // TODO: display message on player HUD
            return false;
        }

        return true;
    }

    protected void clearPoints() {
        
        if (this.player == null) {
            return;
        }

        Measurement data = MEASUREMENTS.get(player);
        if (data != null) {
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

        public double getDistance() {
            
            if (points[0] != null && points[1] != null) {
                return euclidDistance(points[0], points[1]);
            }

            return 0;
        }
    }
}
