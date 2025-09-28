package com.aurus.tinytactics.items;

import java.util.function.Consumer;
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
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.function.ValueLists.OutOfBoundsHandling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TacticsShapeDrawerItem extends Item {
    PlayerEntity player;
    BlockPos origin;

    public TacticsShapeDrawerItem(Item.Settings settings) {
        super(settings.maxCount(1)
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
            if (player.isSneaking()) {
                incrementMode(stack, false);
                return ActionResult.SUCCESS;
            }
            return chooseAction(stack, world, pos, false);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner) {
        if (miner instanceof PlayerEntity playerEntity) {
            this.player = playerEntity;
        } else {
            return false;
        }

        if (!world.isClient) {
            if (player.isSneaking()) {
                incrementMode(stack, true);
            } else {
                chooseAction(stack, world, pos, true);
            }
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent,
            Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.of(stack.get(DataRegistrar.SHAPE_TYPE).asString()));
        textConsumer
                .accept(Text.translatable(this.getTranslationKey() + ".length", stack.get(DataRegistrar.SHAPE_LENGTH)));
        textConsumer.accept(
                Text.translatable(this.getTranslationKey() + ".diameter", stack.get(DataRegistrar.SHAPE_DIAMETER)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        // TODO replace with ComponentTooltipAppenderRegistry
    }

    protected ActionResult chooseAction(ItemStack stack, World world, BlockPos pos, boolean leftClick) {
        switch (stack.get(DataRegistrar.SHAPE_DRAWER_MODE)) {
            case DRAW_START:
                if (!leftClick) {
                    return startDraw(world, stack, pos);
                }
                return this.clearShapes(world, stack.get(DataRegistrar.DYE_COLOR));
            case DRAW_FINISH:
                if (!leftClick) {
                    return finishDraw(world, stack, pos);
                } else {
                    this.origin = null;
                    stack.set(DataRegistrar.SHAPE_DRAWER_MODE, Mode.DRAW_START);
                    return ActionResult.SUCCESS;
                }
            case SHAPE_DIAMETER:
                return this.incrementDimension(stack, DataRegistrar.SHAPE_DIAMETER, leftClick);
            case SHAPE_LENGTH:
                return this.incrementDimension(stack, DataRegistrar.SHAPE_LENGTH, leftClick);
            case SHAPE_TYPE:
                return this.incrementType(stack, leftClick);
            default:
                return ActionResult.PASS;
        }
    }

    protected ActionResult startDraw(World world, ItemStack stack, BlockPos pos) {
        if (this.player == null || this.origin != null) {
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
        sendMessage(player,
                Text.translatable(this.getTranslationKey() + ".dimension", type.toString(), val));
        stack.set(type, val);
        return ActionResult.SUCCESS;
    }

    protected ActionResult incrementMode(ItemStack stack, boolean negative) {
        TacticsShapeDrawerItem.Mode mode = stack.get(DataRegistrar.SHAPE_DRAWER_MODE);
        int modesLength = TacticsShapeDrawerItem.Mode.values().length;
        int index = mode.getIndex();
        if (negative) {
            index--;
            if (index <= TacticsShapeDrawerItem.Mode.DRAW_FINISH.getIndex() && index >= 0) {
                index--;
            }
        } else {
            index++;
            if (index == TacticsShapeDrawerItem.Mode.DRAW_FINISH.getIndex()) {
                index++;
            }
        }
        index = (index + modesLength) % modesLength;
        Mode newMode = Mode.byIndex(index);
        sendMessage(player,
                Text.translatable(this.getTranslationKey() + ".mode", newMode.asString()));
        stack.set(DataRegistrar.SHAPE_DRAWER_MODE, newMode);
        return ActionResult.SUCCESS;
    }

    protected ActionResult incrementType(ItemStack stack, boolean negative) {
        TacticsShape.Type type = stack.get(DataRegistrar.SHAPE_TYPE);
        int modesLength = TacticsShape.Type.values().length;
        int index = type.getIndex();
        if (negative) {
            index--;
        } else {
            index++;
        }
        index = (index + modesLength) % modesLength;
        TacticsShape.Type newType = TacticsShape.Type.byIndex(index);

        sendMessage(player,
                Text.translatable(this.getTranslationKey() + ".type", newType.asString()));
        stack.set(DataRegistrar.SHAPE_TYPE, newType);
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
        this.origin = null;
        stack.set(DataRegistrar.SHAPE_DRAWER_MODE, Mode.DRAW_START);

        return ActionResult.SUCCESS;
    }

    protected void sendNewShape(World world, DyeColor color, TacticsShape shape) {
        TacticsShapeMap currentShapes = world.getAttachedOrCreate(DataRegistrar.TACTICS_SHAPES,
                () -> TacticsShapeMap.DEFAULT);
        ServerHandler.setShapes(world, currentShapes.add(player.getUuid(), color, shape.getType(), shape));
        ServerHandler.broadcastShapeData();
    }

    protected ActionResult clearShapes(World world, DyeColor color) {

        if (this.player == null) {
            return ActionResult.FAIL;
        }

        TacticsShapeMap currentShapes = world.getAttachedOrCreate(DataRegistrar.TACTICS_SHAPES,
                () -> TacticsShapeMap.DEFAULT);
        ServerHandler.setShapes(world, currentShapes.clearColor(player.getUuid(), color));

        ServerHandler.broadcastShapeData();

        this.origin = null;

        return ActionResult.SUCCESS;
    }

    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity) player).sendMessageToClient(message, true);
    }

    public static enum Mode implements StringIdentifiable {
        DRAW_START(0, "draw_start"),
        DRAW_FINISH(1, "draw_finish"),
        SHAPE_TYPE(2, "shape_type"),
        SHAPE_LENGTH(3, "shape_length"),
        SHAPE_DIAMETER(4, "shape_diameter");

        private static final IntFunction<Mode> BY_INDEX = ValueLists.createIndexToValueFunction(Mode::getIndex,
                values(),
                OutOfBoundsHandling.ZERO);

        public int index;
        public String name;

        private Mode(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public int getIndex() {
            return this.index;
        }

        public static Mode byIndex(int index) {
            return (Mode) BY_INDEX.apply(index);
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
