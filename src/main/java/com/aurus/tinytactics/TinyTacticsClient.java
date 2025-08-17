package com.aurus.tinytactics;

import com.aurus.tinytactics.render.VisualManager;

import net.fabricmc.api.ClientModInitializer;

public class TinyTacticsClient implements ClientModInitializer {
    @Override
	public void onInitializeClient() {
		VisualManager.init();
	}
}
