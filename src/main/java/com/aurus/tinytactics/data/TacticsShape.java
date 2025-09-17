package com.aurus.tinytactics.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TacticsShape {

    public final Type type;
    public final BlockPos origin;
    public final Vec3d direction;
    public final double length;
    public final double diameter;

    public static final Codec<TacticsShape> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                Type.CODEC.fieldOf("type").forGetter(TacticsShape::getType),
                BlockPos.CODEC.fieldOf("origin").forGetter(TacticsShape::getOrigin),
                Vec3d.CODEC.fieldOf("direction").forGetter(TacticsShape::getDirection),
                Codec.DOUBLE.fieldOf("length").forGetter(TacticsShape::getLength),
                Codec.DOUBLE.fieldOf("diameter").forGetter(TacticsShape::getDiameter))
                .apply(instance, TacticsShape::new);
    });

    public Type getType() {
        return type;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public Vec3d getDirection() {
        return direction;
    }

    public double getLength() {
        return length;
    }

    public double getDiameter() {
        return diameter;
    }

    public TacticsShape(Type type, BlockPos origin, Vec3d direction, double length, double diameter) {
        this.type = type;
        this.origin = origin;
        this.direction = direction;
        this.length = length;
        this.diameter = diameter;
    }

    public TacticsShape(Type type, BlockPos origin, BlockPos destination, double length, double diameter) {
        this.type = type;
        this.origin = origin;
        this.direction = faceDirection(origin, destination);
        this.length = length;
        this.diameter = diameter;
    }

    public Vec3d faceDirection(BlockPos origin, BlockPos destination) {
        return new Vec3d(destination.getX() - origin.getX(), destination.getY() - origin.getY(),
                destination.getZ() - origin.getZ()).normalize();
    }

    public static enum Type implements StringIdentifiable {
        LINE,
        SPHERE,
        CONE,
        CHAIN;

        public static final Codec<TacticsShape.Type> CODEC = StringIdentifiable.createCodec(TacticsShape.Type::values);

        public String toString() {
            return this.name().toLowerCase();
        }

        @Override
        public String asString() {
            return this.toString();
        }

    }

}