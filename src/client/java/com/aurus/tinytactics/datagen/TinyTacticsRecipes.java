package com.aurus.tinytactics.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.aurus.tinytactics.registry.ItemRegistrar;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class TinyTacticsRecipes extends FabricRecipeProvider {

    public TinyTacticsRecipes(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerDyeableRecipes(exporter, List.of(Items.RED_DYE, Items.ORANGE_DYE, Items.BLUE_DYE, Items.CYAN_DYE), List.of(ItemRegistrar.TACTICS_RULER), "ruler");
    }
    
}
