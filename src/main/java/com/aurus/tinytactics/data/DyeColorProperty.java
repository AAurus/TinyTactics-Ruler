package com.aurus.tinytactics.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.minecraft.state.property.Property;
import net.minecraft.util.DyeColor;

public class DyeColorProperty extends Property<DyeColor> {

    private final List<DyeColor> values = Arrays.asList(DyeColor.values());

    protected DyeColorProperty(String name) {
        super(name, DyeColor.class);
    }

    public static DyeColorProperty of(String name) {
        return new DyeColorProperty(name);
    }

    @Override
    public List<DyeColor> getValues() {
        return this.values;
    }

    @Override
    public String name(DyeColor value) {
        return value.getId();
    }

    @Override
    public Optional<DyeColor> parse(String name) {
        DyeColor color = DyeColor.byId(name, null);
        if (color == null) {
            return Optional.empty();
        } else {
            return Optional.of(color);
        }
    }

    @Override
    public int ordinal(DyeColor value) {
        return value.getIndex();
    }

}
