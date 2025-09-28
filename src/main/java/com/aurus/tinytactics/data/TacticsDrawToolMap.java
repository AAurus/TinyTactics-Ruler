package com.aurus.tinytactics.data;

import com.aurus.tinytactics.util.Collection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.util.DyeColor;

public abstract class TacticsDrawToolMap<T, C extends Collection<T>> {
    protected final Map<UUID, Map<DyeColor, C>> map;
    protected final C empty;

    public TacticsDrawToolMap(Map<UUID, Map<DyeColor, C>> map, C empty) {
        this.map = map;
        this.empty = empty;
    }

    public TacticsDrawToolMap(C empty) {
        this.map = new HashMap<>();
        this.empty = empty;
    }

    public Map<UUID, Map<DyeColor, C>> getFullMap() {
        return map;
    }

    public C getEmpty() {
        return empty;
    }

    @SuppressWarnings("unchecked")
    public Map<UUID, Map<DyeColor, C>> mapAdd(UUID user, DyeColor color, T value) {
        C collection = map.getOrDefault(user, new HashMap<>())
                .getOrDefault(color, empty);
        C newCollection = (C) collection.add(value);
        Map<DyeColor, C> newUserMap = new HashMap<>(map.getOrDefault(user, new HashMap<>()));
        newUserMap.put(color, newCollection);
        Map<UUID, Map<DyeColor, C>> newMap = new HashMap<>(map);
        newMap.put(user, newUserMap);
        return newMap;
    }

    public Map<UUID, Map<DyeColor, C>> mapClearPlayer(UUID user) {
        Map<UUID, Map<DyeColor, C>> newMap = new HashMap<>(map);
        newMap.put(user, new HashMap<>());
        return newMap;
    }

    public Map<UUID, Map<DyeColor, C>> mapClearColor(UUID user, DyeColor color) {
        Map<DyeColor, C> userMap = map.getOrDefault(user, new HashMap<>());
        Map<DyeColor, C> newUserMap = new HashMap<>(userMap);
        newUserMap.put(color, empty);
        Map<UUID, Map<DyeColor, C>> newMap = new HashMap<>(map);
        newMap.put(user, newUserMap);
        return newMap;
    }

}
