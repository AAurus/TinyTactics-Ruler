package com.aurus.tinytactics.data;

import com.aurus.tinytactics.data.TacticsShape.Type;
import com.aurus.tinytactics.util.Collection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

public class TacticsDrawToolMap<T> {
    protected final Map<UUID, Map<DyeColor, Collection<T>>> map;
    protected final Collection<T> empty;

    public TacticsDrawToolMap(Map<UUID, Map<DyeColor, Collection<T>>> map, Collection<T> empty) {
        this.map = map;
        this.empty = empty;
    }

    public Map<UUID, Map<DyeColor, Collection<T>>> getFullMap() {
        return map;
    }

    public TacticsDrawToolMap<T> add(UUID user, DyeColor color, T value) {
        Collection<T> collection = map.getOrDefault(user, new HashMap<>()).getOrDefault(color,
                empty);
        Collection<T> newCollection = collection.add(value);
        Map<DyeColor, Collection<T>> newUserMap = new HashMap<>(map.getOrDefault(user, new HashMap<>()));
        newUserMap.put(color, newCollection);
        Map<UUID, Map<DyeColor, Collection<T>>> newMap = new HashMap<>(map);
        newMap.put(user, newUserMap);
        return new TacticsDrawToolMap<>(newMap, empty);
    }

    public TacticsDrawToolMap<T> clearPlayer(UUID user) {
        Map<UUID, Map<DyeColor, Collection<T>>> newMap = new HashMap<>(map);
        newMap.put(user, new HashMap<>());
        return new TacticsDrawToolMap<>(newMap, empty);
    }

    public TacticsDrawToolMap<T> clearColor(UUID user, DyeColor color) {
        Map<DyeColor, Collection<T>> userMap = map.get(user);
        Map<DyeColor, Collection<T>> newUserMap = new HashMap<>(userMap);
        newUserMap.put(color, empty);
        Map<UUID, Map<DyeColor, Collection<T>>> newMap = new HashMap<>(map);
        newMap.put(user, newUserMap);
        return new TacticsDrawToolMap<>(newMap, empty);
    }

    public Codec<TacticsDrawToolMap<T>> getCodec(String fieldName, Codec<Collection<T>> collectionCodec) {
        return RecordCodecBuilder.create(instance -> instance
                .group(Codec.unboundedMap(Uuids.CODEC, Codec.unboundedMap(DyeColor.CODEC, collectionCodec))
                        .fieldOf(fieldName)
                        .forGetter(TacticsDrawToolMap::getFullMap),
                        collectionCodec.fieldOf("empty").forGetter(null))
                .apply(instance, TacticsDrawToolMap::new));
    }

    public PacketCodec<ByteBuf, TacticsDrawToolMap<T>> getPacketCodec(Codec<TacticsDrawToolMap<T>> codec) {
        return PacketCodecs.codec(codec);
    }
}
