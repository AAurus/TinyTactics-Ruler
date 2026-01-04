package com.aurus.tinytactics.data;

import com.aurus.tinytactics.registry.DataRegistrar;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.dynamic.Codecs;

public record SimpleDyeTintSource(int defaultColor) implements TintSource {

    public static final MapCodec<SimpleDyeTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codecs.RGB.fieldOf("default").forGetter(SimpleDyeTintSource::defaultColor))
            .apply(instance, SimpleDyeTintSource::new));

    @Override
    public int getTint(ItemStack stack, ClientWorld world, LivingEntity user) {
        DyeColor dyeColor = stack.get(DataRegistrar.DYE_COLOR);
        if (dyeColor != null) {
            return dyeColor.getEntityColor();
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }

}
