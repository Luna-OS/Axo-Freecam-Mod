package dev.lunaos.axofreecam.mixins;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import dev.lunaos.axofreecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.gen.Invoker;

import static dev.lunaos.axofreecam.Freecam.MC;

/// When Freecam is active and the ['outlines' feature][Freecam#isOutlineEnabled()]
/// is enabled, ensure the local player is visible and has
/// ['show entity outlines'][LevelRenderState#shouldShowEntityOutlines] enabled.

@Mixin(LevelExtractor.class)
public abstract class EnablePlayerOutlineMixin {

    @Invoker("extractEntity")
    abstract EntityRenderState freecam$extractEntity(final Entity entity, final float partialTickTime);

    @Inject(method = "extractVisibleEntities", at = @At("TAIL"))
    private void onExtractVisibleEntities(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState levelRenderState, CallbackInfo ci) {
        if (Freecam.isEnabled() && Freecam.isOutlineEnabled()) {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
            EntityRenderState state = freecam$extractEntity(MC.player, partialTick);
            state.outlineColor = ARGB.opaque(MC.player.getTeamColor());
            levelRenderState.entityRenderStates.add(state);
            levelRenderState.shouldShowEntityOutlines = true;
        }
    }
}
