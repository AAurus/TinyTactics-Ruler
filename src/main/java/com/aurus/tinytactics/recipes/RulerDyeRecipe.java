package com.aurus.tinytactics.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.World;

public class RulerDyeRecipe implements Recipe<CraftingRecipeInput> {
    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return false;
    }
    @Override
    public ItemStack craft(CraftingRecipeInput input, WrapperLookup lookup) {
        return null;
    }
    @Override
    public boolean fits(int var1, int var2) {
        return false;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
    @Override
    public RecipeType<?> getType() {
        return null;
    }
    @Override
    public ItemStack getResult(WrapperLookup registriesLookup) {
        return null;
    }
}
