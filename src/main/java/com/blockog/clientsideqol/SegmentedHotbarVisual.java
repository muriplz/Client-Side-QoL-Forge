package com.blockog.clientsideqol;

import com.blockog.clientsideqol.config.CSQoLConfig;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultClientPackResources;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public
class SegmentedHotbarVisual {
    private static final Identifier WIDGETS_TEXTURE = new Identifier(ClientSideQoL.MOD_ID, "widgets.png");
    private static final Minecraft client = Minecraft.getInstance();


    public static boolean renderSegmentedHotbar(MatrixStack matrices) {
        CSQoLConfig config = ClientSideQoL.getInstance().config;

        if (!config.segmentedHotbarVisual || isHidden()) return false;

        Player player = (Player) client.cameraEntity;
        assert player != null;
        Inventory inventory = player.getInventory();
        int scaledWidthHalved = client.getWindow().getGuiScaledWidth() / 2 - 30;
        int scaledHeight = client.getWindow().getGuiScaledHeight();

        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        // Draw the hotbar itself
        DrawableHelper.drawTexture(matrices,
                scaledWidthHalved - 65,
                scaledHeight - 22,
                0,
                24,
                190,
                22,
                256,
                64);

        int selectedSection = ClientSideQoL.getInstance().selectedHotbarSection;
        if (selectedSection == -1) { // Draw the regular vanilla selection box
            DrawableHelper.drawTexture(matrices,
                    scaledWidthHalved - 66 + (inventory.selected * 20) + (4 * (inventory.selectedSlot / 3)),
                    scaledHeight - 23,
                    64,
                    0,
                    24,
                    24,
                    256,
                    64);
        } else { // Draw the section-wide selection box
            DrawableHelper.drawTexture(matrices,
                    scaledWidthHalved - 66 + (64 * selectedSection),
                    scaledHeight - 23,
                    0,
                    0,
                    64,
                    24,
                    256,
                    64);
        }

        // Draw hotbar items
        for (int slotNum = 0; slotNum < 9; slotNum++) {
            int x = scaledWidthHalved - 62 + (slotNum * 20) + (4 * (slotNum / 3));
            int y = scaledHeight - 19;
            ItemStack itemStack = inventory.getItem(slotNum);
            client.getItemRenderer().renderGuiItem(itemStack, x, y);
            client.getItemRenderer().renderGuiItemDecorations(client.font, itemStack, x, y);
        }
        return true;
    }

    private static boolean isHidden() {
        if (
                client.interactionManager == null ||
                        Objects.requireNonNull(client.player).isSpectator() ||
                        client.options.hideGui
        ) return true;

        Player player = (Player) client.cameraEntity;
        return player == null || !player.isAlive() || player.playerScreenHandler == null;
    }
}
