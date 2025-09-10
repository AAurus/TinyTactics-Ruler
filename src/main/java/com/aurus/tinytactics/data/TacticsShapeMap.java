package com.aurus.tinytactics.data;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.aurus.tinytactics.data.TacticsShape.Type;
import com.aurus.tinytactics.util.Collection;
import com.aurus.tinytactics.util.MapCollection;

import net.minecraft.util.DyeColor;

public class TacticsShapeMap extends TacticsDrawToolMap<Entry<TacticsShape.Type, TacticsShape>> {

    public TacticsShapeMap(Map<UUID, Map<DyeColor, Collection<Entry<Type, TacticsShape>>>> map) {
        super(map, new MapCollection<>());
    }

    public TacticsDrawToolMap<Entry<TacticsShape.Type, TacticsShape>> add(UUID user, DyeColor color, Type key,
            TacticsShape value) {
        return this.add(user, color, new AbstractMap.SimpleEntry<TacticsShape.Type, TacticsShape>(key, value));
    }
}
