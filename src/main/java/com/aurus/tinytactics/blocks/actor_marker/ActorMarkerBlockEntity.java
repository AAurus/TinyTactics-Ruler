package com.aurus.tinytactics.blocks.actor_marker;

import com.aurus.tinytactics.data.ActorMarkerInventory;
import com.aurus.tinytactics.registry.BlockRegistrar;
import com.aurus.tinytactics.registry.DataRegistrar;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class ActorMarkerBlockEntity extends BlockEntity {

    public ActorMarkerInventory items = ActorMarkerInventory.DEFAULT;

    public ActorMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistrar.ACTOR_MARKER_BLOCK_ENTITY, pos, state);
    }

    public boolean setItem(String key, ItemStack item) {
        ActorMarkerInventory newItems = items.setItem(key, item);
        if (newItems != null) {
            items = newItems;
            markDirty();
            return true;
        }
        return false;
    }

    public ItemStack getItem(String key) {
        return items.getItem(key);
    }

    public boolean hasItem(String key) {
        return items.hasItem(key);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.items = components.getOrDefault(DataRegistrar.ACTOR_MARKER_INVENTORY, ActorMarkerInventory.DEFAULT);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(DataRegistrar.ACTOR_MARKER_INVENTORY, items);
    }

    // @Override
    // protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup
    // lookup) {
    // nbt.put("attachments", items.encode(lookup));
    // super.writeNbt(nbt, lookup);
    // }

    // @Override
    // protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup)
    // {
    // super.readNbt(nbt, lookup);

    // NbtElement nbtItems = nbt.get("attachments");
    // if (nbtItems != null) {
    // items = ActorMarkerInventory.fromNbt(lookup,
    // nbtItems).orElse(ActorMarkerInventory.DEFAULT);
    // }
    // }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup lookup) {
        return createNbt(lookup);
    }

}
