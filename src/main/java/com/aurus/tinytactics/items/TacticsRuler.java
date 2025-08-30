package com.aurus.tinytactics.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.aurus.tinytactics.ServerHandler;
import com.aurus.tinytactics.data.RulerMap;
import com.aurus.tinytactics.registry.DataRegistrar;

public class TacticsRuler extends Item {
    PlayerEntity player;

    public TacticsRuler() {
        super(new Item.Settings().maxCount(1).component(DataRegistrar.DYE_COLOR, DyeColor.WHITE));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        this.player = context.getPlayer();
        World world = context.getWorld();
        DyeColor color = context.getStack().get(DataRegistrar.DYE_COLOR);

        if (player.isSneaking()) {
            return clearPoints(world, color);
        }
        return addPoint(world, color, context.getBlockPos());
    }

    private ActionResult addPoint(World world, DyeColor color, BlockPos pos) {

        RulerMap currentPos = world.getAttachedOrCreate(DataRegistrar.ALL_RULER_POSITIONS, () -> RulerMap.DEFAULT);
        ServerHandler.setPositions(world, currentPos.add(player.getUuid(), color, pos));

        ServerHandler.broadcastPositions();

        return ActionResult.SUCCESS;
    }

    protected ActionResult clearPoints(World world, DyeColor color) {

        if (this.player == null) {
            return ActionResult.FAIL;
        }

        RulerMap currentPos = world.getAttachedOrCreate(DataRegistrar.ALL_RULER_POSITIONS, () -> RulerMap.DEFAULT);
        ServerHandler.setPositions(world, currentPos.clearColor(player.getUuid(), color));

        ServerHandler.broadcastPositions();

        return ActionResult.SUCCESS;
    }
}
