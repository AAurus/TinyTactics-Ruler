package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.components.BlockPosList;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DataRegistrar {

    public static final ComponentType<BlockPosList> RULER_POSITIONS = Registry.register(Registries.DATA_COMPONENT_TYPE, 
        Identifier.of(TinyTactics.MOD_ID, "ruler_positions"), 
        ComponentType.<BlockPosList>builder().codec(BlockPosList.CODEC).build());
}
