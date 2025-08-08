package com.aurus.tinytactics;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aurus.tinytactics.registry.ItemRegistrar;

public class TinyTactics implements ModInitializer {
    public static final String MOD_ID = "tinytactics";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ItemRegistrar.register();

        LOGGER.info("TinyTactics Online!");
    }
}
