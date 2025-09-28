package com.aurus.tinytactics.registry;

import java.util.function.Function;

import com.aurus.tinytactics.TinyTactics;
import com.aurus.tinytactics.items.TacticsRulerItem;
import com.aurus.tinytactics.items.TacticsShapeDrawerItem;

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

    public static final Item TACTICS_RULER = registerItem("tactics_ruler", TacticsRulerItem::new, new Item.Settings());

    public static final Item TACTICS_SHAPE_DRAWER = registerItem("tactics_shape_drawer", TacticsShapeDrawerItem::new,
            new Item.Settings());

    public static final Item[] SIMPLE_DYEABLE_ITEMS = { TACTICS_RULER, TACTICS_SHAPE_DRAWER };

    public static final ItemGroup TINYTACTICS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(TACTICS_RULER))
            .displayName(Text.translatable("itemGroup.tinytactics.group_name"))
            .build();

    public static void registerAll() {
        Registry.register(Registries.ITEM_GROUP, TINYTACTICS_GROUP_KEY, TINYTACTICS_GROUP);

        registerToItemGroup(TINYTACTICS_GROUP_KEY, TACTICS_RULER, TACTICS_SHAPE_DRAWER,
                BlockRegistrar.ACTOR_MARKER.asItem());

    }

    public static Item registerItem(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TinyTactics.MOD_ID, name));
        Item item = factory.apply(settings.registryKey(itemKey));
        return Registry.register(Registries.ITEM, itemKey, item);
    }

    public static void registerToItemGroup(RegistryKey<ItemGroup> groupKey, Item... items) {
        ItemGroupEvents.modifyEntriesEvent(groupKey).register((FabricItemGroupEntries e) -> {
            for (Item i : items) {
                e.add(i);
            }
        });
    }
}
