package com.aurus.tinytactics.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.aurus.tinytactics.ServerHandler;
import com.aurus.tinytactics.data.TacticsRulerMap;
import com.aurus.tinytactics.registry.DataRegistrar;

public class TacticsRulerItem extends Item {
    PlayerEntity player;

    public TacticsRulerItem() {
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

        return addPoint(world, color, context.getBlockPos());
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        if (!world.isClient) {
            this.clearPoints(world, miner.getStackInHand(Hand.MAIN_HAND).get(DataRegistrar.DYE_COLOR));
        }
        return false;
    }

    private ActionResult addPoint(World world, DyeColor color, BlockPos pos) {

        TacticsRulerMap currentPos = world.getAttachedOrCreate(DataRegistrar.TACTICS_RULER_POSITIONS,
                () -> TacticsRulerMap.DEFAULT);
        ServerHandler.setPositions(world, currentPos.add(player.getUuid(), color, pos));

        ServerHandler.broadcastRulerData();

        return ActionResult.SUCCESS;
    }

    protected ActionResult clearPoints(World world, DyeColor color) {

        if (this.player == null) {
            return ActionResult.FAIL;
        }

        TacticsRulerMap currentPos = world.getAttachedOrCreate(DataRegistrar.TACTICS_RULER_POSITIONS,
                () -> TacticsRulerMap.DEFAULT);
        ServerHandler.setPositions(world, currentPos.clearColor(player.getUuid(), color));

        ServerHandler.broadcastRulerData();

        return ActionResult.SUCCESS;
    }
}
