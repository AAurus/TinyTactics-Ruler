package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.components.RulerMap;
import com.aurus.tinytactics.components.RulerMapPayload;
import com.aurus.tinytactics.recipes.SimpleDyeRecipe;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class DataRegistrar {

    public static void registerAll() {
        PayloadTypeRegistry.playS2C().register(RulerMapPayload.ID, RulerMapPayload.CODEC);
        registerRecipeSubtype(SimpleDyeRecipe.Type.INSTANCE, SimpleDyeRecipe.Serializer.INSTANCE, SimpleDyeRecipe.ID);
    }

    public static final ComponentType<DyeColor> DYE_COLOR = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TinyTactics.MOD_ID, "dye_color"),
            ComponentType.<DyeColor>builder().codec(DyeColor.CODEC).build());

    public static final AttachmentType<RulerMap> ALL_RULER_POSITIONS = AttachmentRegistry.create(
            Identifier.of(TinyTactics.MOD_ID, "all_ruler_positions"),
            builder -> builder.initializer(() -> RulerMap.DEFAULT).persistent(RulerMap.CODEC)
                    .syncWith(RulerMap.PACKET_CODEC, AttachmentSyncPredicate.all()));

    public static <T extends Recipe<?>> void registerRecipeSubtype(RecipeType<T> type, RecipeSerializer<T> serializer,
            String id) {
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(TinyTactics.MOD_ID, id), serializer);
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(TinyTactics.MOD_ID, id), type);
    }
}
