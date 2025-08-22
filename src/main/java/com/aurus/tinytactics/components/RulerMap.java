package com.aurus.tinytactics.components;

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

public class RulerMap {
    private final Map<UUID, Map<DyeColor, List<BlockPos>>> posMap;

    public RulerMap(Map<UUID, Map<DyeColor, List<BlockPos>>> map) {
        this.posMap = map;
    }

    public Map<UUID, Map<DyeColor, List<BlockPos>>> getFullMap() {
        return posMap;
    }

    public RulerMap add(UUID user, DyeColor color, BlockPos position) {
        List<BlockPos> posList = posMap.getOrDefault(user, new HashMap<>()).getOrDefault(color, new ArrayList<>());
        List<BlockPos> newPos = new ArrayList<>(posList);
        newPos.add(position);
        Map<DyeColor, List<BlockPos>> newUserPosMap = new HashMap<>(posMap.getOrDefault(user, new HashMap<>()));
        newUserPosMap.put(color, newPos);
        Map<UUID, Map<DyeColor, List<BlockPos>>> newPosMap = new HashMap<>(posMap);
        newPosMap.put(user, newUserPosMap);
        return new RulerMap(newPosMap);
    }

    public RulerMap clearPlayer(UUID user) {
        Map<UUID, Map<DyeColor, List<BlockPos>>> newPosMap = new HashMap<>(posMap);
        newPosMap.put(user, new HashMap<>());
        return new RulerMap(newPosMap);
    }

    public RulerMap clearColor(UUID user, DyeColor color) {
        Map<DyeColor, List<BlockPos>> userMap = posMap.get(user);
        Map<DyeColor, List<BlockPos>> newUserMap = new HashMap<>(userMap);
        newUserMap.put(color, new ArrayList<>());
        Map<UUID, Map<DyeColor, List<BlockPos>>> newMap = new HashMap<>(posMap);
        newMap.put(user, newUserMap);
        return new RulerMap(newMap);
    }

    public static final RulerMap DEFAULT = new RulerMap(new HashMap<>());

    public static final Codec<RulerMap> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Codec.unboundedMap(Uuids.CODEC, Codec.unboundedMap(DyeColor.CODEC, BlockPos.CODEC.listOf()))
                    .fieldOf("posMap")
                    .forGetter(RulerMap::getFullMap))
            .apply(instance, RulerMap::new));

    public static final PacketCodec<ByteBuf, RulerMap> PACKET_CODEC = PacketCodecs.codec(CODEC);
}
