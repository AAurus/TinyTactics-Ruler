package com.aurus.tinytactics.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TinyTacticsDatagen implements DataGeneratorEntrypoint {
    
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(TinyTacticsRecipes::new);
    }

    public String getName() {
        return "TinyTacticsDatagen";
    }
}
