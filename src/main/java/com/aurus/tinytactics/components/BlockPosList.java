package com.aurus.tinytactics.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;

public class BlockPosList {
    private final List<BlockPos> positions;

    public BlockPosList(List<BlockPos> positions) {
        this.positions = Collections.unmodifiableList(positions);
    }

    public List<BlockPos> getPositions() {
        return positions;
    }

    public BlockPosList add(BlockPos position) {
        List<BlockPos> temp = new ArrayList<>(positions);
        temp.add(position);
        return new BlockPosList(temp);
    }

    public static final Codec<BlockPosList> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(BlockPos.CODEC.listOf()
                    .fieldOf("positions")
                    .forGetter(BlockPosList::getPositions))
                .apply(instance, BlockPosList::new));
}
