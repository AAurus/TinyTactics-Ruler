package com.aurus.tinytactics.items;

import com.aurus.tinytactics.registry.DataRegistrar;

import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

public class ColorProviders {

    public static int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 0) {
            return ((DyeColor) stack.get(DataRegistrar.DYE_COLOR)).getEntityColor();
        }
        return -1;
    }
}
