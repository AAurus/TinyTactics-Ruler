package com.aurus.tinytactics.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.aurus.tinytactics.TinyTactics;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;

public class ActorMarkerInventory {

    public static final String LEFT_HAND_KEY = "left_hand_item";
    public static final String RIGHT_HAND_KEY = "right_hand_item";
    public static final String HEAD_KEY = "head_item";
    public static final String ATTACHMENT_KEY = "attachment_item";

    public static final Set<String> KEY_SET = Set.of(LEFT_HAND_KEY, RIGHT_HAND_KEY, HEAD_KEY, ATTACHMENT_KEY);

    public static final ActorMarkerInventory DEFAULT = new ActorMarkerInventory();

    public static final Codec<ActorMarkerInventory> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                Codec.unboundedMap(Codec.STRING, ItemStack.CODEC).fieldOf("items")
                        .forGetter(ActorMarkerInventory::getItemMap))
                .apply(instance, ActorMarkerInventory::new);
    });

    private Map<String, ItemStack> items = new HashMap<>();;

    public ActorMarkerInventory() {
        items = new HashMap<>();
    }

    public ActorMarkerInventory(Map<String, ItemStack> items) {
        this.items = items;
    }

    public Map<String, ItemStack> getItemMap() {
        return items;
    }

    public ActorMarkerInventory setItem(String key, ItemStack item) {
        if (KEY_SET.contains(key)) {
            Map<String, ItemStack> newItems = new HashMap<>(items);
            if (item.isEmpty()) {
                newItems.remove(key);
            } else {
                newItems.put(key, item);
            }
            return new ActorMarkerInventory(newItems);
        }
        return null;
    }

    public ItemStack getItem(String key) {
        return items.getOrDefault(key, ItemStack.EMPTY);
    }

    public boolean hasItem(String key) {
        return (!items.getOrDefault(key, ItemStack.EMPTY).isEmpty());
    }

    public NbtCompound encode(RegistryWrapper.WrapperLookup registries, NbtElement prefix) {
        return (NbtCompound) CODEC.encode(this, registries.getOps(NbtOps.INSTANCE), prefix).getOrThrow();
    }

    public NbtElement encode(RegistryWrapper.WrapperLookup registries) {
        return (NbtElement) CODEC.encodeStart(registries.getOps(NbtOps.INSTANCE), this).getOrThrow();
    }

    public static Optional<ActorMarkerInventory> fromNbt(RegistryWrapper.WrapperLookup registries, NbtElement nbt) {
        return CODEC.parse(registries.getOps(NbtOps.INSTANCE), nbt).resultOrPartial((error) -> {
            TinyTactics.LOGGER.error("Tried to load invalid item inventory: '{}'", error);
        });
    }
}
