package com.aurus.tinytactics.data;

import org.jetbrains.annotations.Nullable;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.registry.DataRegistrar;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class ColorProviders {

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            return ((DyeColor) stack.get(DataRegistrar.DYE_COLOR)).getEntityColor();
        }
        return -1;
    }

    public static int getBlockColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos,
            int tintIndex) {
        TinyTactics.LOGGER.debug("hi!"); // doesn't send
        if (tintIndex == 0) {
            if (state.getBlock().getStateManager().getProperty("color") instanceof DyeColorProperty prop) {
                DyeColor color = ((DyeColor) state.get(prop));
                TinyTactics.LOGGER.debug("this color is {}", color.getName()); // doesn't send either
                return color.getEntityColor();
            } else {
                TinyTactics.LOGGER.debug("couldn't get color property :/"); // nor this
            }
        }
        return -1;
    }

}
