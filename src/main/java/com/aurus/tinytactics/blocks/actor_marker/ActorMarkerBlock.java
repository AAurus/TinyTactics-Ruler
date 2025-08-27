package com.aurus.tinytactics.blocks.actor_marker;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ActorMarkerBlock extends BlockWithEntity {

    public static final int MIN_ROTATION_INDEX = 0;
    public static final int MAX_ROTATION_INDEX = 7;

    public static final IntProperty ROTATION = IntProperty.of("rotation", MIN_ROTATION_INDEX, MAX_ROTATION_INDEX);

    private static final SoundEvent ROTATE_SOUND = SoundEvents.BLOCK_COMPARATOR_CLICK;

    private static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(1.0, 1.0, 1.0, 15.0, 15.0, 15.0);

    public ActorMarkerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ROTATION, 0));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ActorMarkerBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected VoxelShape getCullingShape(BlockState state, BlockView world,
            BlockPos pos) {
        return VoxelShapes.empty();
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(ROTATION, ActorMarkerRotationHelper.fromYaw(context.getPlayerYaw()));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        }

        if (player.getMainHandStack().equals(ItemStack.EMPTY)) {
            world.setBlockState(pos, state.with(ROTATION, (state.get(ROTATION) + 1) % 8));
            world.playSound(player, pos, ROTATE_SOUND, SoundCategory.BLOCKS);
            return ActionResult.SUCCESS;
        } else {
            return equipActorMarker(state, world, pos, player, hit);
        }
    }

    protected ActionResult equipActorMarker(BlockState state, World world, BlockPos pos, PlayerEntity player,
            BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof ActorMarkerBlockEntity actorMarkerBlockEntity) {
            float yawToPlayer = player.getYaw();
            if (yawToPlayer >= 0) {
                yawToPlayer -= 180;
            } else {
                yawToPlayer += 180;
            }

            switch (ActorMarkerRotationHelper.toLocalDirection(yawToPlayer, state.get(ROTATION))) {
                case ActorMarkerRotationHelper.FRONT:
                    actorMarkerBlockEntity.setHeadItem(player.getActiveItem().copy());
                    return ActionResult.SUCCESS;
                case ActorMarkerRotationHelper.LEFT:
                    actorMarkerBlockEntity.setLeftHandItem(player.getActiveItem().copy());
                    return ActionResult.SUCCESS;
                case ActorMarkerRotationHelper.RIGHT:
                    actorMarkerBlockEntity.setRightHandItem(player.getActiveItem().copy());
                    return ActionResult.SUCCESS;
                case ActorMarkerRotationHelper.BACK:
                    actorMarkerBlockEntity.setAttachmentItem(player.getActiveItem().copy());
                    return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(ActorMarkerBlock::new);
    }

}
