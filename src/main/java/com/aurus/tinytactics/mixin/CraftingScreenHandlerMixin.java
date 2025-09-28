package com.aurus.tinytactics.mixin;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aurus.tinytactics.recipes.SimpleDyeRecipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {

    @Inject(method = "updateResult", at = @At("TAIL"))
    private static void injectSimpleDyeSupport(
            ScreenHandler handler, ServerWorld world, PlayerEntity player,
            RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory,
            @Nullable RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci) {
        if (world.isClient() || !resultInventory.getStack(0).isEmpty()) {
            return;
        }

        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        CraftingRecipeInput input = craftingInventory.createRecipeInput();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

        Optional<RecipeEntry<SimpleDyeRecipe>> match = server.getRecipeManager()
                .getFirstMatch(SimpleDyeRecipe.Type.INSTANCE, input, world);

        if (match.isPresent()) {
            RecipeEntry<SimpleDyeRecipe> recipeEntry = match.get();
            SimpleDyeRecipe matchRecipe = recipeEntry.value();

            if (resultInventory.shouldCraftRecipe(serverPlayer, recipeEntry)) {
                ItemStack result = matchRecipe.craft(input, world.getRegistryManager());
                if (result.isItemEnabled(world.getEnabledFeatures())) {
                    resultInventory.setStack(0, result);
                    // handler.setPreviousTrackedSlot(0, result); //figure out whatever this did
                    // later
                    serverPlayer.networkHandler.sendPacket(
                            new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, result));
                }
            }
        }

    }

}
