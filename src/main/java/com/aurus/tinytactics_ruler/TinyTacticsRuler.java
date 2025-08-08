package com.aurus.tinytactics_ruler;

import net.fabricmc.api.ModInitializer;

import com.aurus.tinytactics_ruler.registry.ItemRegistrar;
import com.aurus.tinytactics_ruler.client.VisualManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyTacticsRuler implements ModInitializer {
    public static final String MOD_ID = "tinytactics-ruler";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        VisualManager.init();
        ItemRegistrar.register();

        LOGGER.info("TinyTactics Ruler Online!");
    }
}
