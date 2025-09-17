package com.aurus.tinytactics.items;

import java.util.function.IntFunction;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;

import com.aurus.tinytactics.ServerHandler;
import com.aurus.tinytactics.data.TacticsShape;
import com.aurus.tinytactics.data.TacticsShapeMap;
import com.aurus.tinytactics.registry.DataRegistrar;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.function.ValueLists.OutOfBoundsHandling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TacticsShapeDrawerItem extends Item {
    PlayerEntity player;
    BlockPos origin;

    public TacticsShapeDrawerItem() {
        super(new Settings().maxCount(1)
                .component(DataRegistrar.DYE_COLOR, DyeColor.WHITE)
                .component(DataRegistrar.SHAPE_TYPE, TacticsShape.Type.LINE)
                .component(DataRegistrar.SHAPE_LENGTH, 0)
                .component(DataRegistrar.SHAPE_DIAMETER, 0)
                .component(DataRegistrar.SHAPE_DRAWER_MODE, Mode.DRAW_START));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        this.player = context.getPlayer();
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();

        if (!world.isClient) {
            return chooseAction(stack, world, pos, false);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        this.player = miner;
        ItemStack stack = miner.getStackInHand(Hand.MAIN_HAND);

        if (!world.isClient) {
            chooseAction(stack, world, pos, true);
        }
        return false;
    }

    protected ActionResult chooseAction(ItemStack stack, World world, BlockPos pos, boolean leftClick) {
        switch (stack.get(DataRegistrar.SHAPE_DRAWER_MODE)) {
            case DRAW_START:
                if (!leftClick) {
                    return startDraw(world, stack, pos);
                }
            case DRAW_FINISH:
                if (!leftClick) {
                    return finishDraw(world, stack, pos);
                } else {
                    return this.clearShapes(world, stack.get(DataRegistrar.DYE_COLOR));
                }
            case SHAPE_DIAMETER:
                return this.incrementDimension(stack, DataRegistrar.SHAPE_DIAMETER, leftClick);
            case SHAPE_LENGTH:
                return this.incrementDimension(stack, DataRegistrar.SHAPE_LENGTH, leftClick);
            case SHAPE_TYPE:
                return this.incrementMode(stack, leftClick);
            default:
                return ActionResult.PASS;
        }
    }

    protected ActionResult startDraw(World world, ItemStack stack, BlockPos pos) {
        if (this.player == null || this.origin == null) {
            return ActionResult.FAIL;
        }

        origin = pos;
        stack.set(DataRegistrar.SHAPE_DRAWER_MODE, Mode.DRAW_FINISH);

        return ActionResult.SUCCESS;
    }

    protected ActionResult incrementDimension(ItemStack stack, ComponentType<Integer> type, boolean negative) {
        int val = stack.get(type);
        if (negative) {
            val--;
            if (val < 0) {
                return ActionResult.FAIL;
            }
        } else {
            val++;
        }
        stack.set(type, val);
        return ActionResult.SUCCESS;
    }

    protected ActionResult incrementMode(ItemStack stack, boolean negative) {
        TacticsShapeDrawerItem.Mode mode = stack.get(DataRegistrar.SHAPE_DRAWER_MODE);
        int modesLength = Mode.values().length;
        int id;
        if (negative) {
            id = (mode.getId() - 1 + modesLength) % modesLength;
            if (id <= Mode.DRAW_FINISH.getId()) {
                id--;
                id = (id + modesLength) % modesLength;
            }
        } else {
            id = (mode.getId() + 1) % modesLength;
            if (id == Mode.DRAW_FINISH.getId()) {
                id++;
                id = (id + modesLength) % modesLength;
            }
        }
        stack.set(DataRegistrar.SHAPE_DRAWER_MODE, Mode.byId(id));
        return ActionResult.SUCCESS;
    }

    protected ActionResult finishDraw(World world, ItemStack stack, BlockPos pos) {
        if (this.player == null || this.origin == null) {
            return ActionResult.FAIL;
        }

        DyeColor color = stack.get(DataRegistrar.DYE_COLOR);
        TacticsShape.Type type = stack.get(DataRegistrar.SHAPE_TYPE);
        int length = stack.get(DataRegistrar.SHAPE_LENGTH);
        int diameter = stack.get(DataRegistrar.SHAPE_DIAMETER);

        sendNewShape(world, color, new TacticsShape(type, origin, pos, length, diameter));
        stack.set(DataRegistrar.SHAPE_DRAWER_MODE, Mode.DRAW_START);

        return ActionResult.SUCCESS;
    }

    protected void sendNewShape(World world, DyeColor color, TacticsShape shape) {
        TacticsShapeMap currentShapes = world.getAttachedOrCreate(DataRegistrar.TACTICS_SHAPES,
                () -> TacticsShapeMap.DEFAULT);
        ServerHandler.setShapes(world, currentShapes.add(player.getUuid(), color, shape.getType(), shape));
    }

    protected ActionResult clearShapes(World world, DyeColor color) {

        if (this.player == null) {
            return ActionResult.FAIL;
        }

        TacticsShapeMap currentShapes = world.getAttachedOrCreate(DataRegistrar.TACTICS_SHAPES,
                () -> TacticsShapeMap.DEFAULT);
        ServerHandler.setShapes(world, currentShapes.clearColor(player.getUuid(), color));

        ServerHandler.broadcastRulerData();

        return ActionResult.SUCCESS;
    }

    public static enum Mode implements StringIdentifiable {
        DRAW_START(0, "draw_start"),
        DRAW_FINISH(1, "draw_finish"),
        SHAPE_TYPE(2, "shape_type"),
        SHAPE_LENGTH(3, "shape_length"),
        SHAPE_DIAMETER(4, "shape_diameter");

        private static final IntFunction<Mode> BY_ID = ValueLists.createIdToValueFunction(Mode::getId, values(),
                OutOfBoundsHandling.ZERO);

        public int id;
        public String name;

        private Mode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public static Mode byId(int id) {
            return (Mode) BY_ID.apply(id);
        }

        public static final Codec<TacticsShapeDrawerItem.Mode> CODEC = StringIdentifiable
                .createCodec(TacticsShapeDrawerItem.Mode::values);

        public String toString() {
            return this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Nullable
        @Contract("_,!null->!null;_,null->_")
        public static Mode byName(String name, @Nullable Mode defaultMode) {
            Mode mode = (Mode) valueOf(Mode.class, name);
            return mode != null ? mode : defaultMode;
        }

    }

}
