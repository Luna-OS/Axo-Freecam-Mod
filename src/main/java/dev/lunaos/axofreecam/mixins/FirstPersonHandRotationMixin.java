package dev.lunaos.axofreecam.mixins;

import net.minecraft.client.player.LocalPlayer;
import dev.lunaos.axofreecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.util.Mth;
import dev.lunaos.axofreecam.util.FreeCamera;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// Make first-person hand & held item view bobbing & rotation use the camera,
/// instead of the player.

@Mixin(FirstPersonHandsAndItems.class)
public class FirstPersonHandRotationMixin {


    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void onExtractRenderState(LocalPlayer player, float partialTicks, FirstPersonHandsAndItemsRenderState state, CallbackInfo ci) {
        if (Freecam.isEnabled()) {
            FreeCamera camera = Freecam.getFreeCamera();
            state.viewXRot = camera.getViewXRot(partialTicks);
            state.viewYRot = camera.getViewYRot(partialTicks);
            state.xBob = Mth.lerp(partialTicks, camera.xBobO(), camera.xBob());
            state.yBob = Mth.lerp(partialTicks, camera.yBobO(), camera.yBob());
        }
    }

}
