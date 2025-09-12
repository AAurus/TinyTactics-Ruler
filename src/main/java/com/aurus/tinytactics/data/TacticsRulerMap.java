package com.aurus.tinytactics.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aurus.tinytactics.util.ListCollection;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

public class TacticsRulerMap extends TacticsDrawToolMap<BlockPos, ListCollection<BlockPos>> {

    public static final ListCollection<BlockPos> EMPTY = new ListCollection<>();
    public static final TacticsRulerMap DEFAULT = new TacticsRulerMap(new HashMap<>());

    public static final Codec<TacticsRulerMap> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Codec.unboundedMap(Uuids.CODEC, Codec.unboundedMap(DyeColor.CODEC, EMPTY.getCodec(BlockPos.CODEC)))
                    .fieldOf("tactics_ruler_positions")
                    .forGetter(TacticsDrawToolMap::getFullMap),
                    EMPTY.getCodec(BlockPos.CODEC).fieldOf("empty")
                            .forGetter(TacticsDrawToolMap::getEmpty))
            .apply(instance, TacticsRulerMap::new));
    public static final PacketCodec<ByteBuf, TacticsRulerMap> PACKET_CODEC = PacketCodecs
            .codec(CODEC);

    public TacticsRulerMap(Map<UUID, Map<DyeColor, ListCollection<BlockPos>>> map, ListCollection<BlockPos> empty) {
        super(map, empty);
    }

    public TacticsRulerMap(Map<UUID, Map<DyeColor, ListCollection<BlockPos>>> map) {
        super(map, EMPTY);
    }

    public TacticsRulerMap(TacticsDrawToolMap<BlockPos, ListCollection<BlockPos>> data) {
        super(data.getFullMap(), data.getEmpty());
    }

    public Map<UUID, Map<DyeColor, ListCollection<BlockPos>>> getFullMap() {
        return super.getFullMap();
    }

    public ListCollection<BlockPos> getEmpty() {
        return super.getEmpty();
    }

    public TacticsRulerMap add(UUID user, DyeColor color, BlockPos value) {
        return new TacticsRulerMap(mapAdd(user, color, value));
    }

    public TacticsRulerMap clearPlayer(UUID user) {
        return new TacticsRulerMap(mapClearPlayer(user));
    }

    public TacticsRulerMap clearColor(UUID user, DyeColor color) {
        return new TacticsRulerMap(mapClearColor(user, color));
    }

}
