package com.aurus.tinytactics.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;

public class SimpleDyeRecipeSerializer implements RecipeSerializer<SimpleDyeRecipe> {

    public static final SimpleDyeRecipeSerializer INSTANCE = new SimpleDyeRecipeSerializer();

    private static final MapCodec<SimpleDyeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(SimpleDyeRecipe::getGroup),
            CraftingRecipeCategory.CODEC.fieldOf("category").forGetter(SimpleDyeRecipe::getCategory),
            Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(SimpleDyeRecipe::getIngredient))
            .apply(instance, SimpleDyeRecipe::new));

    private static final PacketCodec<RegistryByteBuf, SimpleDyeRecipe> PACKET_CODEC = PacketCodec
            .ofStatic(SimpleDyeRecipeSerializer::write, SimpleDyeRecipeSerializer::read);

    private SimpleDyeRecipeSerializer() {
    }

    @Override
    public MapCodec<SimpleDyeRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, SimpleDyeRecipe> packetCodec() {
        return PACKET_CODEC;
    }

    private static SimpleDyeRecipe read(RegistryByteBuf buf) {
        String group = buf.readString();
        CraftingRecipeCategory category = (CraftingRecipeCategory) buf.readEnumConstant(CraftingRecipeCategory.class);
        Ingredient ingredient = (Ingredient) Ingredient.PACKET_CODEC.decode(buf);

        return new SimpleDyeRecipe(group, category, ingredient);
    }

    private static void write(RegistryByteBuf buf, SimpleDyeRecipe recipe) {
        buf.writeString(recipe.group);
        buf.writeEnumConstant(recipe.category);
        Ingredient.PACKET_CODEC.encode(buf, recipe.ingredient);
    }
}
