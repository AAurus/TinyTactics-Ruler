package com.aurus.tinytactics.items;

import com.aurus.tinytactics.registry.DataRegistrar;

import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;

public class TacticsShapeDrawer extends Item {

    public TacticsShapeDrawer() {
        super(new Settings().maxCount(1).component(DataRegistrar.DYE_COLOR, DyeColor.WHITE));
    }

}
