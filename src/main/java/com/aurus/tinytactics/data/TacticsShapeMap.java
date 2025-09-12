package com.aurus.tinytactics.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aurus.tinytactics.util.MapCollection;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Uuids;

public class TacticsShapeMap extends
        TacticsDrawToolMap<Pair<TacticsShape.Type, TacticsShape>, MapCollection<TacticsShape.Type, TacticsShape>> {

    public static final TacticsShapeMap DEFAULT = new TacticsShapeMap(new HashMap<>());
    public static final MapCollection<TacticsShape.Type, TacticsShape> EMPTY = new MapCollection<>();

    public static final Codec<TacticsDrawToolMap<Pair<TacticsShape.Type, TacticsShape>, MapCollection<TacticsShape.Type, TacticsShape>>> CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(Codec
                            .unboundedMap(Uuids.CODEC,
                                    Codec.unboundedMap(DyeColor.CODEC,
                                            EMPTY.getCodec(TacticsShape.Type.CODEC, TacticsShape.CODEC)))
                            .fieldOf("tactics_shapes")
                            .forGetter(TacticsDrawToolMap::getFullMap),
                            EMPTY.getCodec(TacticsShape.Type.CODEC, TacticsShape.CODEC).fieldOf("empty")
                                    .forGetter(TacticsDrawToolMap::getEmpty))
                    .apply(instance, TacticsShapeMap::new));

    public TacticsShapeMap(Map<UUID, Map<DyeColor, MapCollection<TacticsShape.Type, TacticsShape>>> map,
            MapCollection<TacticsShape.Type, TacticsShape> empty) {
        super(map, empty);
    }

    public TacticsShapeMap(Map<UUID, Map<DyeColor, MapCollection<TacticsShape.Type, TacticsShape>>> map) {
        super(map, EMPTY);
    }

    public TacticsShapeMap add(UUID user, DyeColor color, TacticsShape.Type key, TacticsShape value) {
        return new TacticsShapeMap(mapAdd(user, color, new Pair<>(key, value)));
    }

    public TacticsShapeMap clearPlayer(UUID user) {
        return new TacticsShapeMap(mapClearPlayer(user));
    }

    public TacticsShapeMap clearColor(UUID user, DyeColor color) {
        return new TacticsShapeMap(mapClearColor(user, color));
    }
}
