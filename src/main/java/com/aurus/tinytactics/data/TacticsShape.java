package com.aurus.tinytactics.data;

import java.util.function.IntFunction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.SpyglassItem;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.function.ValueLists.OutOfBoundsHandling;
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
        LINE(0, "line"),
        SPHERE(1, "sphere"),
        CONE(2, "cone");

        private int id;
        private String name;

        private static final IntFunction<Type> BY_ID = ValueLists.createIdToValueFunction(Type::getId, values(),
                OutOfBoundsHandling.ZERO);

        private Type(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static final Codec<TacticsShape.Type> CODEC = StringIdentifiable.createCodec(TacticsShape.Type::values);

        public int getId() {
            return this.id;
        }

        public static Type byId(int id) {
            return (Type) BY_ID.apply(id);
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Nullable
        @Contract("_,!null->!null;_,null->_")
        public static Type byName(String name, @Nullable Type defaultType) {
            Type Type = (Type) valueOf(Type.class, name);
            return Type != null ? Type : defaultType;
        }

    }

}