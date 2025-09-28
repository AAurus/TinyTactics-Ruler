package com.aurus.tinytactics.recipes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.aurus.tinytactics.registry.DataRegistrar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.AdvancementRequirements.CriterionMerger;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.World;

public class SimpleDyeRecipe implements CraftingRecipe {

    public static final String ID = "simple_dye";

    protected final String group;
    protected final ItemStack item;
    protected final CraftingRecipeCategory category;

    public SimpleDyeRecipe(String group, CraftingRecipeCategory category, ItemStack item) {
        this.group = group;
        this.category = category;
        this.item = item;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack dyeableStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack queryStack = input.getStackInSlot(i);
            if (!queryStack.isEmpty()) {
                if (!dyeStack.isEmpty() || !(queryStack.getItem() instanceof DyeItem)) {
                    if (!dyeableStack.isEmpty() || !item.getItem().equals(queryStack.getItem())) {
                        return false;
                    }

                    dyeableStack = queryStack;
                } else {
                    dyeStack = queryStack;
                }
            }
        }

        return !dyeableStack.isEmpty() && !dyeStack.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, WrapperLookup lookup) {

        ItemStack dyeableStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack queryStack = input.getStackInSlot(i);
            if (!queryStack.isEmpty()) {
                if (!dyeStack.isEmpty() || !(queryStack.getItem() instanceof DyeItem)) {
                    if (!dyeableStack.isEmpty() || !item.getItem().equals(queryStack.getItem())) {
                        return ItemStack.EMPTY;
                    }

                    dyeableStack = queryStack;
                } else {
                    dyeStack = queryStack;
                }
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
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getItem() {
        return this.item;
    }

    // @Override
    // public RecipeType<CraftingRecipe> getType() {
    // return Type.INSTANCE;
    // } //TODO figure out how to re-implement crafting recipe subtype

    @Override
    public RecipeSerializer<SimpleDyeRecipe> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<SimpleDyeRecipe> {
        public static final SimpleDyeRecipe.Type INSTANCE = new SimpleDyeRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<SimpleDyeRecipe> {

        public static final Serializer INSTANCE = new SimpleDyeRecipe.Serializer();

        private static final MapCodec<SimpleDyeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "")
                        .forGetter(SimpleDyeRecipe::getGroup),
                CraftingRecipeCategory.CODEC.fieldOf("category")
                        .forGetter(SimpleDyeRecipe::getCategory),
                ItemStack.CODEC.fieldOf("item")
                        .forGetter(SimpleDyeRecipe::getItem))
                .apply(instance, SimpleDyeRecipe::new));

        private static final PacketCodec<RegistryByteBuf, SimpleDyeRecipe> PACKET_CODEC = PacketCodec
                .ofStatic(Serializer::write, Serializer::read);

        private Serializer() {
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
            CraftingRecipeCategory category = (CraftingRecipeCategory) buf
                    .readEnumConstant(CraftingRecipeCategory.class);
            ItemStack item = (ItemStack) ItemStack.PACKET_CODEC.decode(buf);

            return new SimpleDyeRecipe(group, category, item);
        }

        private static void write(RegistryByteBuf buf, SimpleDyeRecipe recipe) {
            buf.writeString(recipe.group);
            buf.writeEnumConstant(recipe.category);
            ItemStack.PACKET_CODEC.encode(buf, recipe.item);
        }
    }

    public static class JsonBuilder implements CraftingRecipeJsonBuilder {

        private final RecipeCategory category;
        private final Item input;
        private final Map<String, AdvancementCriterion<?>> advancementBuilder = new LinkedHashMap<>();
        @Nullable
        private String group;

        public JsonBuilder(RecipeCategory category, ItemConvertible input) {
            this.category = category;
            this.input = input.asItem();
        }

        @Override
        public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
            this.advancementBuilder.put(name, criterion);
            return this;
        }

        @Override
        public Item getOutputItem() {
            return this.input;
        }

        @Override
        public CraftingRecipeJsonBuilder group(String group) {
            this.group = group;
            return this;
        }

        private void validate(RegistryKey<Recipe<?>> recipeKey) {
            if (this.advancementBuilder.isEmpty()) {
                throw new IllegalStateException("Impossible to obtain recipe " + recipeKey);
            }
        }

        @Override
        public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
            this.validate(recipeKey);
            Advancement.Builder builder = exporter.getAdvancementBuilder()
                    .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey))
                    .rewards(AdvancementRewards.Builder.recipe(recipeKey)).criteriaMerger(CriterionMerger.OR);
            this.advancementBuilder.forEach(builder::criterion);
            SimpleDyeRecipe recipe = new SimpleDyeRecipe(Objects.requireNonNull(this.group, ""),
                    CraftingRecipeJsonBuilder.toCraftingCategory(this.category), new ItemStack(this.input, 1));
            exporter.accept(recipeKey, recipe,
                    builder.build(
                            recipeKey.getRegistry().withPrefixedPath("recipes/" + this.category.getName() + "/")));
        }

    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forShapeless(new ArrayList<>()); // TODO add dyeItem and item to list
    }

}
