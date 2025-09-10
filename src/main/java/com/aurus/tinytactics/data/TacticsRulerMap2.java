package com.aurus.tinytactics.data;

import java.util.Map;
import java.util.UUID;

import com.aurus.tinytactics.util.Collection;
import com.aurus.tinytactics.util.ListCollection;

import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class TacticsRulerMap2 extends TacticsDrawToolMap<BlockPos> {

    public TacticsRulerMap2(Map<UUID, Map<DyeColor, Collection<BlockPos>>> map,
            Collection<BlockPos> empty) {
        super(map, new ListCollection<>());
    }

}
