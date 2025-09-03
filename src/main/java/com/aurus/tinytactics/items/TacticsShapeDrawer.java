package com.aurus.tinytactics.items;

import com.aurus.tinytactics.data.ShapeType;
import com.aurus.tinytactics.registry.DataRegistrar;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TacticsShapeDrawer extends Item {

    public TacticsShapeDrawer() {
        super(new Settings().maxCount(1)
                .component(DataRegistrar.DYE_COLOR, DyeColor.WHITE)
                .component(DataRegistrar.SHAPE_TYPE, ShapeType.LINE)
                .component(DataRegistrar.SHAPE_LENGTH, 0)
                .component(DataRegistrar.SHAPE_DIAMETER, 0));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // TODO Auto-generated method stub
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // TODO Auto-generated method stub
        return super.postMine(stack, world, state, pos, miner);
    }

}
