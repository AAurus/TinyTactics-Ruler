package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.components.BlockPosList;
import com.aurus.tinytactics.components.BlockPosMap;
import com.aurus.tinytactics.components.BlockPosMapPayload;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DataRegistrar {

    public static final ComponentType<BlockPosList> RULER_POSITIONS = Registry.register(Registries.DATA_COMPONENT_TYPE, 
        Identifier.of(TinyTactics.MOD_ID, "ruler_positions"), 
        ComponentType.<BlockPosList>builder().codec(BlockPosList.CODEC).build());

    public static final AttachmentType<BlockPosMap> ALL_RULER_POSITIONS = AttachmentRegistry.create(
        Identifier.of(TinyTactics.MOD_ID, "all_ruler_positions"), 
            builder -> builder.initializer(() -> BlockPosMap.DEFAULT).persistent(BlockPosMap.CODEC).syncWith(BlockPosMap.PACKET_CODEC, AttachmentSyncPredicate.all()));

    public static void registerAll() {
        PayloadTypeRegistry.playS2C().register(BlockPosMapPayload.ID, BlockPosMapPayload.CODEC);
    }
}
