package com.aurus.tinytactics.registry;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.items.TacticsRuler;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemRegistrar {

    public static final RegistryKey<ItemGroup> TINYTACTICS_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),
            Identifier.of(TinyTactics.MOD_ID, "tinytactics_group"));

    public static final Item TACTICS_RULER = new TacticsRuler();

    public static final Item[] SIMPLE_DYEABLE_ITEMS = { TACTICS_RULER };

    public static final ItemGroup TINYTACTICS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(TACTICS_RULER))
            .displayName(Text.translatable("itemGroup.tinytactics.group_name"))
            .build();

    public static void registerAll() {

        registerItem("tactics_ruler", TACTICS_RULER);

        Registry.register(Registries.ITEM_GROUP, TINYTACTICS_GROUP_KEY, TINYTACTICS_GROUP);

        ItemGroupEvents.modifyEntriesEvent(TINYTACTICS_GROUP_KEY).register((FabricItemGroupEntries e) -> {
            ItemRegistrar.registerToItemGroup(e, TACTICS_RULER, BlockRegistrar.ACTOR_MARKER.asItem(),
                    BlockRegistrar.TEST_BLOCK.asItem());
        });

    }

    public static Item registerItem(String name, Item item) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TinyTactics.MOD_ID, name));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void registerToItemGroup(FabricItemGroupEntries itemGroup, Item... items) {
        for (Item i : items) {
            itemGroup.add(i);
        }
    }
}
