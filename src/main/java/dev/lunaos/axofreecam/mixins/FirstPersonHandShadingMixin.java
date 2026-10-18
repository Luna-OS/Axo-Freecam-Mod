package dev.lunaos.axofreecam.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import dev.lunaos.axofreecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;

import static dev.lunaos.axofreecam.Freecam.MC;

/// Makes first-person hand & held-item shading use FreeCamera position,
/// rather than player position.

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandShadingMixin {

    @ModifyVariable(
        method = "submitArmWithItem",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0)
    private int onSubmitArmWithItem_lightCoordsArg(int lightCoords, @Local(argsOnly = true, ordinal = 0) float partialTick) {
        if (Freecam.isEnabled()) {
            return MC.getEntityRenderDispatcher().getPackedLightCoords(Freecam.getFreeCamera(), partialTick);
        }
        return lightCoords;
    }
}
