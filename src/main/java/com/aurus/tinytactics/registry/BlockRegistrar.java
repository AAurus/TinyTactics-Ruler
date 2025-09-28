package com.aurus.tinytactics.registry;

import java.util.function.Function;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlock;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class BlockRegistrar {

    public static final Block ACTOR_MARKER = registerBlock("actor_marker",
            ActorMarkerBlock::new, AbstractBlock.Settings.create(), true);

    public static final BlockEntityType<ActorMarkerBlockEntity> ACTOR_MARKER_BLOCK_ENTITY = registerBlockEntity(
            "actor_marker", ActorMarkerBlockEntity::new, ACTOR_MARKER);

    public static final Block[] SIMPLE_DYEABLE_BLOCKS = { ACTOR_MARKER };

    public static void registerAll() {
    }

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(TinyTactics.MOD_ID, name);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block block = factory.apply(settings.registryKey(blockKey));
        Block result = Registry.register(Registries.BLOCK, id, block);
        if (shouldRegisterItem) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, id, blockItem);
        }
        return result;
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name,
            FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(TinyTactics.MOD_ID, name),
                FabricBlockEntityTypeBuilder.<T>create(factory, blocks).build());
    }
}
