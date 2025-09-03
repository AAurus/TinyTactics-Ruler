package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.data.ActorMarkerInventory;
import com.aurus.tinytactics.data.RulerMap;
import com.aurus.tinytactics.data.RulerMapPayload;
import com.aurus.tinytactics.data.ShapeType;
import com.aurus.tinytactics.recipes.SimpleDyeRecipe;
import com.mojang.serialization.Codec;

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
import net.minecraft.util.dynamic.Codecs;

public class DataRegistrar {

    public static void registerAll() {
        PayloadTypeRegistry.playS2C().register(RulerMapPayload.ID, RulerMapPayload.CODEC);
        registerRecipeSubtype(SimpleDyeRecipe.Type.INSTANCE, SimpleDyeRecipe.Serializer.INSTANCE, SimpleDyeRecipe.ID);
    }

    public static final ComponentType<DyeColor> DYE_COLOR = registerComponentType("dye_color", DyeColor.CODEC);

    public static final ComponentType<ShapeType> SHAPE_TYPE = registerComponentType("shape_type", ShapeType.CODEC);

    public static final ComponentType<Integer> SHAPE_LENGTH = registerComponentType("shape_length",
            Codecs.POSITIVE_INT);

    public static final ComponentType<Integer> SHAPE_DIAMETER = registerComponentType("shape_diameter",
            Codecs.POSITIVE_INT);

    public static final ComponentType<ActorMarkerInventory> ACTOR_MARKER_INVENTORY = Registry.register(
            Registries.DATA_COMPONENT_TYPE, Identifier.of(TinyTactics.MOD_ID, "actor_marker_inventory"),
            ComponentType.<ActorMarkerInventory>builder().codec(ActorMarkerInventory.CODEC).build());

    public static final AttachmentType<RulerMap> ALL_RULER_POSITIONS = AttachmentRegistry.create(
            Identifier.of(TinyTactics.MOD_ID, "all_ruler_positions"),
            builder -> builder.initializer(() -> RulerMap.DEFAULT).persistent(RulerMap.CODEC)
                    .syncWith(RulerMap.PACKET_CODEC, AttachmentSyncPredicate.all()));

    public static <T extends Recipe<?>> void registerRecipeSubtype(RecipeType<T> type, RecipeSerializer<T> serializer,
            String id) {
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(TinyTactics.MOD_ID, id), serializer);
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(TinyTactics.MOD_ID, id), type);
    }

    public static <T> ComponentType<T> registerComponentType(String name, Codec<T> codec) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(TinyTactics.MOD_ID, name),
                ComponentType.<T>builder().codec(codec).build());
    }
}
