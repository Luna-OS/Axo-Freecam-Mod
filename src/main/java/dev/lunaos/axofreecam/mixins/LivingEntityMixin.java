package dev.lunaos.axofreecam.mixins;

import net.minecraft.world.entity.LivingEntity;
import dev.lunaos.axofreecam.Freecam;
import dev.lunaos.axofreecam.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.lunaos.axofreecam.Freecam.MC;
import static dev.lunaos.axofreecam.config.model.FlightMode.CREATIVE;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract float getHealth();

    // Allows for the horizontal speed of creative flight to be configured separately from vertical speed.
    @Inject(method = "getFrictionInfluencedSpeed", at = @At("HEAD"), cancellable = true)
    private void onGetMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (Freecam.isEnabled() && ModConfig.get().getFlightMode().equals(CREATIVE) && freecam$this() == Freecam.getFreeCamera()) {
            cir.setReturnValue((float) (ModConfig.get().getHorizontalSpeed() / 10) * (Freecam.getFreeCamera().isSprinting() ? 2 : 1));
        }
    }

    // Disables freecam upon receiving damage if disableOnDamage is enabled.
    @Inject(method = "setHealth", at = @At("HEAD"))
    private void onSetHealth(float health, CallbackInfo ci) {
        if (Freecam.isEnabled() && ModConfig.get().shouldDisableOnDamage() && freecam$this() == MC.player) {
            if (!MC.player.isCreative() && getHealth() > health) {
                Freecam.disableNextTick();
            }
        }
    }

    @Unique
    private LivingEntity freecam$this() {
        return (LivingEntity) (Object) this;
    }
}
