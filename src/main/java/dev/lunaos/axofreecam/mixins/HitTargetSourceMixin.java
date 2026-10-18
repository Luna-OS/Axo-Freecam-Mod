package dev.lunaos.axofreecam.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import dev.lunaos.axofreecam.Freecam;
import dev.lunaos.axofreecam.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.lunaos.axofreecam.Freecam.MC;

import net.minecraft.client.Minecraft;

/// When [`player control`][Freecam#isPlayerControlEnabled()] is enabled or `interaction mode` is [`player`][ModConfig#allowInteractionsFromPlayer()],
/// ensure mouse clicks come from the player rather than the camera.

@Mixin(Minecraft.class)
public class HitTargetSourceMixin {
    @ModifyExpressionValue(method = "pick(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
    private Entity onGetHitTargetSource(Entity original) {
        return Freecam.isEnabled() && (Freecam.isPlayerControlEnabled() || ModConfig.get().allowInteractionsFromPlayer())
            ? MC.player
            : original;
    }
}
