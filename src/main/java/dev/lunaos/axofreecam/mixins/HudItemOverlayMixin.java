package dev.lunaos.axofreecam.mixins;

import net.minecraft.resources.Identifier;
import dev.lunaos.axofreecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/// Prevent rendering equipped-item overlays while Freecam is active

@Mixin(Hud.class)
public class HudItemOverlayMixin {

    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderTextureOverlay(
            GuiGraphicsExtractor graphics,
            //PoseStack poseStack,
            Identifier texture,
            float alpha,
            CallbackInfo ci) {
        if (Freecam.isEnabled()) {
            ci.cancel();
        }
    }
}
