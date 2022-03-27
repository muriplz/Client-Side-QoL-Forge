package com.blockog.clientsideqol.mixins;

import com.blockog.clientsideqol.ClientSideQoL;
import com.mojang.authlib.minecraft.client.MinecraftClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow public LocalPlayer player;

    @Shadow @Final public GameConfig options;
    @Shadow @Nullable public  Screen currentScreen;

    @Redirect(method = "handleInputEvents",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z",
            ordinal = 2))
    private boolean handleHotbarSlotSelection(InputEvent.KeyInputEvent keyBinding) {
        if (!keyBinding.wasPressed())
            return false;
        if (player.isSpectator())
            return true;

        if (!player.isCreative() || currentScreen != null || (!options.saveToolbarActivatorKey.isPressed() && !options.loadToolbarActivatorKey.isPressed()))
            for (int i = 0; i < 9; ++i) {
                if (keyBinding == options.hotbarKeys[i])
                    return !ClientSideQoL.getInstance().handleSegmentedHotbarSlotSelection(player.getInventory(), i);
            }
        return true;
    }

    @Redirect(method = "doItemUse",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Hand;values()[Lnet/minecraft/util/Hand;"))
    private Hand[] inventorioDoItemUse()
    {
        if (player == null)
            return new Hand[]{};
        return ClientSideQoL.getInstance().handleItemUsage(player);
    }
}