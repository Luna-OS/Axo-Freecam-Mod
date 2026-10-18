package dev.lunaos.axofreecam.mixins;

import dev.lunaos.axofreecam.Freecam;
import dev.lunaos.axofreecam.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.Lightmap;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(Lightmap.class)
public class FullBrightMixin {

    @Unique
    private boolean freecam$useFullBright() {
        return Freecam.isEnabled() && ModConfig.get().isFullBrightEnabled();
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "FIELD", opcode = GETFIELD, target = "Lnet/minecraft/client/renderer/state/LightmapRenderState;brightness:F"))
    private float onGetBrightness(float original) {
        return freecam$useFullBright() ? 16.0f : original;
    }
}
