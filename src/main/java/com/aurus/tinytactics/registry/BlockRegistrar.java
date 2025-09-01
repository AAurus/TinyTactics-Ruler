package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlock;
import com.aurus.tinytactics.blocks.actor_marker.ActorMarkerBlockEntity;
import com.aurus.tinytactics.blocks.test_block.TestBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistrar {

    public static final Block ACTOR_MARKER = registerBlock("actor_marker",
            new ActorMarkerBlock(AbstractBlock.Settings.create()), true);

    public static final BlockEntityType<ActorMarkerBlockEntity> ACTOR_MARKER_BLOCK_ENTITY = registerBlockEntity(
            "actor_marker", ActorMarkerBlockEntity::new, ACTOR_MARKER);

    public static final Block TEST_BLOCK = registerBlock("test_block", new TestBlock(AbstractBlock.Settings.create()),
            true);

    public static final Block[] SIMPLE_DYEABLE_BLOCKS = { ACTOR_MARKER };

    public static void registerAll() {
    }

    private static Block registerBlock(String name, Block block, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(TinyTactics.MOD_ID, name);
        Block result = Registry.register(Registries.BLOCK, id, block);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        return result;
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name,
            BlockEntityType.BlockEntityFactory<T> factory, Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(TinyTactics.MOD_ID, name),
                BlockEntityType.Builder.<T>create(factory, blocks).build());
    }
}
