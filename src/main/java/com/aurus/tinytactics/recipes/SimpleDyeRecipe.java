package com.aurus.tinytactics.recipes;

import com.aurus.tinytactics.registry.DataRegistrar;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.World;

public class SimpleDyeRecipe implements CraftingRecipe {

    protected final String group;
    protected final Ingredient ingredient;
    protected final CraftingRecipeCategory category;

    public SimpleDyeRecipe(String group, CraftingRecipeCategory category, Ingredient ingredient) {
        this.group = group;
        this.category = category;
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack dyeableStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for (int i = 0; i < input.getSize(); ++i) {
            ItemStack queryStack = input.getStackInSlot(i);
            if (!queryStack.isEmpty()) {
                if (!(queryStack.getItem() instanceof DyeItem)) {
                    if (!ingredient.test(dyeStack) || !dyeableStack.isEmpty()) {
                        return false;
                    }

                    dyeableStack = queryStack;
                }

                dyeStack = queryStack;
            }
        }

        return !dyeableStack.isEmpty() && !dyeStack.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, WrapperLookup lookup) {
        ItemStack dyeableStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for (int i = 0; i < input.getSize(); ++i) {
            ItemStack queryStack = input.getStackInSlot(i);
            if (!queryStack.isEmpty()) {
                if (!(queryStack.getItem() instanceof DyeItem)) {
                    if (!ingredient.test(dyeStack) || !dyeableStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    dyeableStack = queryStack;
                }

                dyeStack = queryStack;
            }
        }

        if (!dyeableStack.isEmpty() && !dyeStack.isEmpty()) {
            ItemStack resultStack = dyeableStack.copy();
            resultStack.set(DataRegistrar.DYE_COLOR, ((DyeItem) dyeStack.getItem()).getColor());
            return resultStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResult(WrapperLookup registriesLookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    public String getGroup() {
        return this.group;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleDyeRecipeSerializer.INSTANCE;
    }

}
