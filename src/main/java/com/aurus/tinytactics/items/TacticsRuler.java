package com.aurus.tinytactics.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.aurus.tinytactics.ServerHandler;
import com.aurus.tinytactics.components.BlockPosMap;
import com.aurus.tinytactics.registry.DataRegistrar;

public class TacticsRuler extends Item {
    PlayerEntity player;

    public TacticsRuler() {
        super(new Item.Settings().maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        this.player = context.getPlayer();
        World world = context.getWorld();

        if (player.isSneaking()) {
            return clearPoints(world);
        }
        return addPoint(world, context.getBlockPos());
    }

    private ActionResult addPoint(World world, BlockPos pos) {

        BlockPosMap currentPos = world.getAttachedOrCreate(DataRegistrar.ALL_RULER_POSITIONS, () -> BlockPosMap.DEFAULT);
        ServerHandler.setPositions(world, currentPos.add(player.getUuid(), pos));

        ServerHandler.broadcastPositions();

        return ActionResult.SUCCESS;
    }

    protected ActionResult clearPoints(World world) {
        
        if (this.player == null) {
            return ActionResult.FAIL;
        }

        BlockPosMap currentPos = world.getAttachedOrCreate(DataRegistrar.ALL_RULER_POSITIONS, () -> BlockPosMap.DEFAULT);
        ServerHandler.setPositions(world, currentPos.clearPlayer(player.getUuid()));

        ServerHandler.broadcastPositions();

        return ActionResult.SUCCESS;
    }
}
