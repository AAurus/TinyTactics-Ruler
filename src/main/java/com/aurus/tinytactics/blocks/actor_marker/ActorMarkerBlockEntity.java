package com.aurus.tinytactics.blocks.actor_marker;

import java.util.HashMap;
import java.util.Map;

import com.aurus.tinytactics.registry.BlockRegistrar;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class ActorMarkerBlockEntity extends BlockEntity {

    private Map<String, ItemStack> items = Map.of(
            "LEFT_HAND", ItemStack.EMPTY,
            "RIGHT_HAND", ItemStack.EMPTY,
            "HEAD", ItemStack.EMPTY,
            "ATTACHMENT", ItemStack.EMPTY);

    public ActorMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistrar.ACTOR_MARKER_BLOCK_ENTITY, pos, state);
    }

    public ItemStack getLeftHandItem() {
        return items.getOrDefault("LEFT_HAND", ItemStack.EMPTY);
    }

    public ItemStack getRightHandItem() {
        return items.getOrDefault("RIGHT_HAND", ItemStack.EMPTY);
    }

    public ItemStack getHeadItem() {
        return items.getOrDefault("HEAD", ItemStack.EMPTY);
    }

    public ItemStack getAttachmentItem() {
        return items.getOrDefault("ATTACHMENT", ItemStack.EMPTY);
    }

    public void setLeftHandItem(ItemStack item) {
        setItem("LEFT_HAND", item.copy());
        markDirty();
    }

    public void setRightHandItem(ItemStack item) {
        setItem("RIGHT_HAND", item.copy());
        markDirty();
    }

    public void setHeadItem(ItemStack item) {
        setItem("HEAD", item.copy());
        markDirty();
    }

    public void setAttachmentItem(ItemStack item) {
        setItem("ATTACHMENT", item.copy());
        markDirty();
    }

    public boolean setItem(String key, ItemStack item) {
        if (items.keySet().contains(key)) {
            Map<String, ItemStack> newItems = new HashMap<>(items);
            newItems.put(key, item);
            items = newItems;
            return true;
        }
        return false;
    }

    public ItemStack getItem(String key) {
        return items.getOrDefault(key, ItemStack.EMPTY);
    }

    public boolean hasItem(String key) {
        return (!items.getOrDefault(key, ItemStack.EMPTY).equals(ItemStack.EMPTY));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        encodeItemOrEmpty(nbt, lookup, getLeftHandItem(), "left_hand_item");
        encodeItemOrEmpty(nbt, lookup, getRightHandItem(), "right_hand_item");
        encodeItemOrEmpty(nbt, lookup, getHeadItem(), "head_item");
        encodeItemOrEmpty(nbt, lookup, getAttachmentItem(), "attachment_item");

        super.writeNbt(nbt, lookup);
    }

    private void encodeItemOrEmpty(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup, ItemStack item, String key) {
        if (!item.isEmpty()) {
            nbt.put(key, item.encode(lookup));
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);

        setLeftHandItem(ItemStack.fromNbt(lookup, nbt.get("left_hand_item")).orElse(ItemStack.EMPTY));
        setRightHandItem(ItemStack.fromNbt(lookup, nbt.get("right_hand_item")).orElse(ItemStack.EMPTY));
        setHeadItem(ItemStack.fromNbt(lookup, nbt.get("head_item")).orElse(ItemStack.EMPTY));
        setAttachmentItem(ItemStack.fromNbt(lookup, nbt.get("attachment_item")).orElse(ItemStack.EMPTY));
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup lookup) {
        return createNbt(lookup);
    }
}
