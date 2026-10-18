package dev.lunaos.axofreecam.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.player.Player;
import dev.lunaos.axofreecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.Hud;

import static dev.lunaos.axofreecam.Freecam.MC;

/// Makes HUD correspond to the player, rather than the camera.

@Mixin(Hud.class)
public class HudPlayerMixin {

    @ModifyReturnValue(method = "getCameraPlayer", at = @At("RETURN"))
    private Player onGetCameraPlayer(Player original) {
        return Freecam.isEnabled() ? MC.player : original;
    }
}
