package com.blockog.clientsideqol.mixins;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 99)
@OnlyIn(Dist.CLIENT)

public class SHVMixin {

    @Inject(method = "renderHotbar", at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void inventorioRenderSegmentedHotbar(float l1, PoseStack j1, CallbackInfo ci)
    {
        if (SegmentedHotbarVisual.renderSegmentedHotbar(j1))
            ci.cancel();
    }
}
