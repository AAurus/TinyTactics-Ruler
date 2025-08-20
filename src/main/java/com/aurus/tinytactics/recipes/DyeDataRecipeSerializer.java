package com.aurus.tinytactics.recipes;

import org.spongepowered.include.com.google.gson.Gson;

import com.aurus.tinytactics.recipes.DyeDataRecipe.DyeDataRecipeJsonFormat;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class DyeDataRecipeSerializer implements RecipeSerializer<DyeDataRecipe> {

    @Override
    public MapCodec<DyeDataRecipe> codec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'codec'");
    }

    @Override
    public PacketCodec<RegistryByteBuf, DyeDataRecipe> packetCodec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'packetCodec'");
    }

    @Override
    public DyeDataRecipe read(Identifier id, JsonObject json) {
        DyeDataRecipeJsonFormat recipeJson = new Gson().fromJson(json, DyeDataRecipeJsonFormat.class);
        Ingredient dyeInput = Ingredient.fromJson(recipeJson.dyeInput);
        Ingredient dyeableInput = Ingredient.fromJson(recipeJson.dyeableInput);

        return new DyeDataRecipe(dyeInput, dyeableInput, id);
    }
    
}
