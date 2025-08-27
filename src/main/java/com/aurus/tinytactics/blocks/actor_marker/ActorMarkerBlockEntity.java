package com.aurus.tinytactics.blocks.actor_marker;

import com.aurus.tinytactics.registry.BlockRegistrar;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class ActorMarkerBlockEntity extends BlockEntity {

    private ItemStack leftHandItem = ItemStack.EMPTY;
    private ItemStack rightHandItem = ItemStack.EMPTY;
    private ItemStack headItem = ItemStack.EMPTY;
    private ItemStack attachmentItem = ItemStack.EMPTY;

    public ActorMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistrar.ACTOR_MARKER_BLOCK_ENTITY, pos, state);
    }

    public ItemStack getLeftHandItem() {
        return leftHandItem;
    }

    public ItemStack getRightHandItem() {
        return rightHandItem;
    }

    public ItemStack getHeadItem() {
        return headItem;
    }

    public ItemStack getAttachmentItem() {
        return attachmentItem;
    }

    public void setLeftHandItem(ItemStack item) {
        leftHandItem = item.copy();
        markDirty();
    }

    public void setRightHandItem(ItemStack item) {
        rightHandItem = item.copy();
        markDirty();
    }

    public void setHeadItem(ItemStack item) {
        headItem = item.copy();
        markDirty();
    }

    public void setAttachmentItem(ItemStack item) {
        attachmentItem = item.copy();
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        encodeItemOrEmpty(nbt, lookup, leftHandItem, "left_hand_item");
        encodeItemOrEmpty(nbt, lookup, rightHandItem, "right_hand_item");
        encodeItemOrEmpty(nbt, lookup, headItem, "head_item");
        encodeItemOrEmpty(nbt, lookup, attachmentItem, "attachment_item");

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

        leftHandItem = ItemStack.fromNbt(lookup, nbt.get("left_hand_item")).orElse(ItemStack.EMPTY);
        rightHandItem = ItemStack.fromNbt(lookup, nbt.get("right_hand_item")).orElse(ItemStack.EMPTY);
        headItem = ItemStack.fromNbt(lookup, nbt.get("head_item")).orElse(ItemStack.EMPTY);
        attachmentItem = ItemStack.fromNbt(lookup, nbt.get("attachment_item")).orElse(ItemStack.EMPTY);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup lookup) {
        return createNbt(lookup);
    }
}
