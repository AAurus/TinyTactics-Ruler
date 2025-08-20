package com.aurus.tinytactics.recipes;

import java.util.List;

import com.aurus.tinytactics.registry.DataRegistrar;
import com.google.gson.JsonObject;

import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DyeDataRecipe implements Recipe<CraftingRecipeInput> {

    private final Ingredient dyeInput;
    private final Ingredient dyeableInput;
    private final Identifier id;

    public DyeDataRecipe(Ingredient dyeInput, Ingredient dyeableInput, Identifier id) {
        this.dyeInput = dyeInput;
        this.dyeableInput = dyeableInput;
        this.id = id;
    }

    public Ingredient getDyeInput() {
        return dyeInput;
    }

    public Ingredient getDyeableInput() {
        return dyeableInput;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        
        if (input.getStackCount() == 2) {

            List<ItemStack> stacks = input.getStacks();
            Item dyeItemRaw = null;
            boolean hasDyeable = false;

            for (ItemStack stack : stacks) {
                if (dyeInput.test(stack)) {
                    dyeItemRaw = stack.getItem();
                }
                if (dyeableInput.test(stack)) {
                    hasDyeable = true;
                }
            }

            if (dyeItemRaw != null) {
                try {
                DyeItem dyeItem = (DyeItem) dyeItemRaw;

                return (dyeItem != null) && hasDyeable;

                } catch (ClassCastException e) {
                    return false;
                }
            }

        }

        return false;

    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, WrapperLookup lookup) {
        
        List<ItemStack> stacks = input.getStacks();
        Item dyeItemRaw = null;
        ItemStack dyeableItem = null;

        for (ItemStack stack : stacks) {
            if (dyeInput.test(stack)) {
                dyeItemRaw = stack.getItem();
            }
            if (dyeableInput.test(stack)) {
                dyeableItem = stack;
            }
        }

        ItemStack resultItem = dyeableItem.copy();
        DyeColor color = getDyeColorOrNull(dyeItemRaw);
        if (color != null) {
            resultItem.set(DataRegistrar.DYE_COLOR, color);
        }
        return resultItem;

    }
    @Override
    public boolean fits(int var1, int var2) {
        return (var1 + var2 == 3);
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
    @Override
    public RecipeType<?> getType() {
        return DyeDataRecipeType.INSTANCE;
    }
    @Override
    public ItemStack getResult(WrapperLookup registriesLookup) {
        return null;
    }

    private DyeColor getDyeColorOrNull(Item item) {
        
        try {
            DyeItem dyeItem = (DyeItem) item;
            return dyeItem.getColor();
        } catch (ClassCastException e) {
            return null;
        }

    }

    public class DyeDataRecipeJsonFormat {
        JsonObject dyeInput;
        JsonObject dyeableInput;
    }

    public static class DyeDataRecipeType implements RecipeType<DyeDataRecipe> {
        
        private DyeDataRecipeType() {}

        public static final DyeDataRecipeType INSTANCE = new DyeDataRecipeType();

        public static final String ID = "dye_data_recipe";
    }

}
