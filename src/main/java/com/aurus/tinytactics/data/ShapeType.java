package com.aurus.tinytactics.data;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringIdentifiable;

public enum ShapeType implements StringIdentifiable {
    LINE,
    SPHERE,
    CONE,
    CHAIN;

    public static final Codec<ShapeType> CODEC = StringIdentifiable.createCodec(ShapeType::values);

    public String toString() {
        return this.name().toLowerCase();
    }

    @Override
    public String asString() {
        return this.toString();
    }

}
