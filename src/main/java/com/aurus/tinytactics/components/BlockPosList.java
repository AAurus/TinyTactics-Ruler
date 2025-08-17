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

    public static double getDistance(BlockPosList positions) {
        double result = 0;
        List<BlockPos> blockPositions = positions.getPositions();
        for (int i = 1; i < blockPositions.size(); i++) {
            result += euclidDistance(blockPositions.get(i-1), blockPositions.get(i));
        } 
        return result;
    }

    protected static double euclidDistance(BlockPos a, BlockPos b) {
        
        int dx = b.getX() - a.getX();
        int dy = b.getY() - a.getY();
        int dz = b.getZ() - a.getZ();

        return Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2) + Math.pow(dz,2));
    }

    public static final BlockPosList DEFAULT = new BlockPosList(new ArrayList<>());

    public static final Codec<BlockPosList> CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(BlockPos.CODEC.listOf()
                    .fieldOf("positions")
                    .forGetter(BlockPosList::getPositions))
                .apply(instance, BlockPosList::new));
}
