package com.aurus.tinytactics.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import net.minecraft.state.property.Property;
import net.minecraft.util.DyeColor;

public class DyeColorProperty extends Property<DyeColor> {

    private final ImmutableSet<DyeColor> values = ImmutableSet.copyOf(Arrays.asList(DyeColor.values()));

    protected DyeColorProperty(String name) {
        super(name, DyeColor.class);
    }

    public static DyeColorProperty of(String name) {
        return new DyeColorProperty(name);
    }

    @Override
    public Collection<DyeColor> getValues() {
        return this.values;
    }

    @Override
    public String name(DyeColor value) {
        return value.getName();
    }

    @Override
    public Optional<DyeColor> parse(String name) {
        DyeColor color = DyeColor.byName(name, null);
        if (color == null) {
            return Optional.empty();
        } else {
            return Optional.of(color);
        }
    }

}
