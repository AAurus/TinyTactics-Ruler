package com.aurus.tinytactics.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

public class BlockPosMap {
    private final Map<UUID, BlockPosList> posMap;

    public BlockPosMap(Map<UUID, BlockPosList> map) {
        this.posMap = map;
    }

    public Map<UUID, BlockPosList> getPosmap() {
        return posMap;
    }

    public BlockPosMap add(UUID user, BlockPos position) {
        BlockPosList newPos = posMap.getOrDefault(user, BlockPosList.DEFAULT).add(position);
        Map<UUID, BlockPosList> newPosMap = new HashMap<>(posMap);
        newPosMap.put(user, newPos);
        return new BlockPosMap(newPosMap);
    }

    public BlockPosMap clearPlayer(UUID user) {
        Map<UUID, BlockPosList> newPosMap = new HashMap<>(posMap);
        newPosMap.put(user, BlockPosList.DEFAULT);
        return new BlockPosMap(newPosMap);
    }

    public static final BlockPosMap DEFAULT = new BlockPosMap(new HashMap<>());

    public static final Codec<BlockPosMap> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(Codec.unboundedMap(Uuids.CODEC, BlockPosList.CODEC)
                    .fieldOf("posMap")
                    .forGetter(BlockPosMap::getPosmap))
                .apply(instance, BlockPosMap::new));

    public static final PacketCodec<ByteBuf, BlockPosMap> PACKET_CODEC = PacketCodecs.codec(CODEC);
}
