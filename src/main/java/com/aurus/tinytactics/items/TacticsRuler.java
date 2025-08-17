package com.aurus.tinytactics.items;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aurus.tinytactics.components.BlockPosList;
import com.aurus.tinytactics.components.BlockPosMap;
import com.aurus.tinytactics.components.BlockPosMapPayload;
import com.aurus.tinytactics.registry.DataRegistrar;

public class TacticsRuler extends Item {
    protected static final Map<UUID, BlockPosList> MEASUREMENTS = new HashMap<>();
    PlayerEntity player;

    public TacticsRuler() {
        super(new Item.Settings().maxCount(1).component(DataRegistrar.RULER_POSITIONS, BlockPosList.DEFAULT));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        this.player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (player.isSneaking()) {
            clearPoints(stack);
            MEASUREMENTS.remove(player.getUuid());
        }
        else {
            MEASUREMENTS.computeIfAbsent(player.getUuid(), p -> new BlockPosList(new ArrayList<>()));

            BlockPos pos = context.getBlockPos();
            BlockPosList positions = stack.getOrDefault(DataRegistrar.RULER_POSITIONS, new BlockPosList(new ArrayList<>()));
            stack.set(DataRegistrar.RULER_POSITIONS, positions.add(pos));
            MEASUREMENTS.put(player.getUuid(), positions.add(pos));
        }

        return sendMeasure(context);
    }

    private ActionResult sendMeasure(ItemUsageContext context) {
        
        BlockPosMapPayload payload = new BlockPosMapPayload(new BlockPosMap(MEASUREMENTS));

        for (ServerPlayerEntity serverPlayer : PlayerLookup.world((ServerWorld) context.getWorld())) {
            ServerPlayNetworking.send(serverPlayer, payload);
        }

        return ActionResult.SUCCESS;
    }

    protected void clearPoints(ItemStack stack) {
        
        if (this.player == null) {
            return;
        }

        BlockPosList data = MEASUREMENTS.get(player.getUuid());
        if (data != null) {
            stack.set(DataRegistrar.RULER_POSITIONS, new BlockPosList(new ArrayList<>()));
        }
    }
}
