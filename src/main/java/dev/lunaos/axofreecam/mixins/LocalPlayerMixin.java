package dev.lunaos.axofreecam.mixins;

import net.minecraft.client.player.LocalPlayer;
import dev.lunaos.axofreecam.Freecam;
import dev.lunaos.axofreecam.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.lunaos.axofreecam.Freecam.MC;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends EntityMixin {

    // Needed for Baritone compatibility.
    @Inject(method = "isControlledCamera", at = @At("HEAD"), cancellable = true)
    private void onIsCamera(CallbackInfoReturnable<Boolean> cir) {
        if (Freecam.isEnabled() && freecam$this() == MC.player) {
            cir.setReturnValue(true);
        }
    }

    // Makes rotation depend upon FreeCamera rather than the player.
    @Override
    protected void onGetViewXRot(float partialTick, CallbackInfoReturnable<Float> cir) {
        if (freecam$useFreecamRotation()) {
            cir.setReturnValue(Freecam.getFreeCamera().getViewXRot(partialTick));
        }
    }

    // Makes rotation depend upon FreeCamera rather than the player.
    @Inject(method = "getViewYRot", at = @At("HEAD"), cancellable = true)
    private void onGetViewYRot(float partialTick, CallbackInfoReturnable<Float> cir) {
        if (freecam$useFreecamRotation()) {
            cir.setReturnValue(Freecam.getFreeCamera().getViewYRot(partialTick));
        }
    }

    @Unique
    private boolean freecam$useFreecamRotation() {
        return Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !ModConfig.get().allowInteractionsFromPlayer();
    }

    @Unique
    private LocalPlayer freecam$this() {
        return (LocalPlayer) (Object) this;
    }
}
