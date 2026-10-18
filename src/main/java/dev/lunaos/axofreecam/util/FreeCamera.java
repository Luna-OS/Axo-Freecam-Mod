package dev.lunaos.axofreecam.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec2;
import dev.lunaos.axofreecam.config.ModConfig;
import dev.lunaos.axofreecam.config.model.Perspective;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.MoveSimulationType;
import net.minecraft.client.player.ClientInput;
import net.minecraft.core.Holder;

import java.util.UUID;
import java.util.function.BooleanSupplier;

import static dev.lunaos.axofreecam.Freecam.MC;

@ApiStatus.Internal
@ApiStatus.AvailableSince("0.4.0")
public class FreeCamera extends AbstractClientPlayer {

    private final BooleanSupplier hasInput;
    private final KeyboardInput keyboardInput;
    private final ClientInput dummyInput;

    private float yBob;
    private float xBob;
    private float yBobO;
    private float xBobO;

    public FreeCamera(int id, BooleanSupplier hasInputPredicate) {
        super(MC.level, new GameProfile(UUID.randomUUID(), "FreeCamera"));

        setId(id);
        setPose(Pose.SWIMMING);
        getAbilities().flying = true;
        hasInput = hasInputPredicate;
        keyboardInput = new KeyboardInput(MC.options);
        dummyInput = new ClientInput();
    }

    @Override
    public void tick() {
        getInput().tick(
        );
        doMotion();
        super.tick();
    }

    @Override
    public void copyPosition(@NotNull Entity entity) {
        applyPosition(new FreecamPosition(entity));
    }

    public void applyPosition(FreecamPosition position) {
        snapTo(position.x, position.y, position.z, position.yaw, position.pitch);
        xBob = getXRot();
        yBob = getYRot();
        xBobO = xBob; // Prevents camera from rotating upon entering freecam.
        yBobO = yBob;
    }

    // Mutate the position and rotation based on perspective
    // If checkCollision is true, move as far as possible without colliding
    public void applyPerspective(Perspective perspective, boolean checkCollision) {
        FreecamPosition position = new FreecamPosition(this);

        switch (perspective) {
            case INSIDE:
                // No-op
                break;
            case FIRST_PERSON:
                // Move just in front of the player's eyes
                moveForwardUntilCollision(position, 0.4, checkCollision);
                break;
            case THIRD_PERSON_MIRROR:
                // Invert the rotation and fallthrough into the THIRD_PERSON case
                position.mirrorRotation();
            case THIRD_PERSON:
                // Move back as per F5 mode
                moveForwardUntilCollision(position, -4.0, checkCollision);
                break;
        }
    }

    // Move FreeCamera forward using FreecamPosition.moveForward.
    // If checkCollision is true, stop moving forward before hitting a collision.
    // Return true if successfully able to move.
    private boolean moveForwardUntilCollision(FreecamPosition position, double distance, boolean checkCollision) {
        if (!checkCollision) {
            position.moveForward(distance);
            applyPosition(position);
            return true;
        }
        return moveForwardUntilCollision(position, distance);
    }

    // Same as above, but always check collision.
    private boolean moveForwardUntilCollision(FreecamPosition position, double maxDistance) {
        boolean negative = maxDistance < 0;
        maxDistance = negative ? -1 * maxDistance : maxDistance;
        double increment = 0.1;

        // Move forward by increment until we reach maxDistance or hit a collision
        for (double distance = 0.0; distance < maxDistance; distance += increment) {
            FreecamPosition oldPosition = new FreecamPosition(this);

            position.moveForward(negative ? -1 * increment : increment);
            applyPosition(position);

            if (!wouldNotSuffocateAtTargetPose(getPose())) {
                // Revert to last non-colliding position and return whether we were unable to move at all
                applyPosition(oldPosition);
                return distance > 0;
            }
        }

        return true;
    }

    private ClientLevel getClientLevel() {
        return (ClientLevel) level();
    }

    public void spawn() {
        getClientLevel().addEntity(this);
    }

    public void despawn() {
        getClientLevel().removeEntity(getId(), RemovalReason.DISCARDED);
    }

    // Prevents fall damage sound when FreeCamera touches ground with noClip disabled.
    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, @NotNull BlockState landedState, @NotNull BlockPos landedPosition) {
    }

    // Needed for hand swings to be shown in freecam since the player is replaced by FreeCamera in HeldItemRenderer.renderItem()
    @Override
    public @Nullable SwingDescription getCurrentSwing() {
        return MC.player.getCurrentSwing();
    }

    @Override
    public float getSwingAnimation(final float partialTicks) {
        return MC.player.getSwingAnimation(partialTicks);
    }

    @Override
    public boolean isSwinging() {
        return MC.player.isSwinging();
    }

    // Needed for item use animations to be shown in freecam since the player is replaced by FreeCamera in HeldItemRenderer.renderItem()
    @Override
    public int getUseItemRemainingTicks() {
        return MC.player.getUseItemRemainingTicks();
    }

    // Also needed for item use animations to be shown in freecam.
    @Override
    public boolean isUsingItem() {
        return MC.player.isUsingItem();
    }

    // Prevents slow down from ladders/vines.
    @Override
    public boolean onClimbable() {
        return false;
    }

    // Prevents slow down from water.
    @Override
    public boolean isInWater() {
        return false;
    }

    // Makes night vision apply to FreeCamera when Iris is enabled.
    @Override
    public MobEffectInstance getEffect(@NotNull Holder<MobEffect> effect) {
        return MC.player.getEffect(effect);
    }

    // Prevents pistons from moving FreeCamera when collision.ignoreAll is enabled.
    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        if (ModConfig.get().ignoreAllCollision()) {
            return PushReaction.IGNORE_ENTITY;
        }
        return super.getPistonPushReaction();
    }

    // Prevents collision with solid entities (shulkers, boats)
    @Override
    public boolean canCollideWith(@NotNull Entity other) {
        return false;
    }

    // Ensures that the FreeCamera is always in the swimming pose.
    @Override
    public void setPose(@NotNull Pose pose) {
        super.setPose(Pose.SWIMMING);
    }

    // Prevents water submersion sounds from playing.
    @Override
    protected boolean updateIsUnderwater() {
        this.wasUnderwater = this.isEyeInFluid(FluidTags.WATER);
        return this.wasUnderwater;
    }

    // Prevents water submersion sounds from playing.
    @Override
    protected void doWaterSplashEffect() {}

    private void doMotion() {
        switch (ModConfig.get().getFlightMode()) {
            case DEFAULT -> {
                getAbilities().setFlyingSpeed(0);
                Motion.doMotion(this, ModConfig.get().getHorizontalSpeed(), ModConfig.get().getVerticalSpeed());
            }
            case CREATIVE -> {
                getAbilities().setFlyingSpeed((float) ModConfig.get().getVerticalSpeed() / 10);

                if (this.isSneakKeyDown() ^ this.isJumpKeyDown()) {
                    int direction = this.isJumpKeyDown() ? 1 : -1;
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0F, ((float) direction * this.getAbilities().getFlyingSpeed() * 3.0F), 0.0F));
                }
            }
        }
        getAbilities().flying = true;
        setOnGround(false);
    }

    @Override
    public float getViewXRot(float partialTick) {
        return this.getXRot();
    }

    @Override
    public float getViewYRot(float partialTick) {
        return this.getYRot();
    }

    // In newer versions, this also enables movement ticking (like below)
    @Override
    public boolean isEffectiveAi() {
        return true;
    }

    // Enabling movement ticking
    @Override
    public @NotNull MoveSimulationType getMoveSimulationType() {
        return MoveSimulationType.SERVER_AND_CLIENT;
    }

    @Override
    protected void applyInput() {
        Vec2 moveVector = getInput().getMoveVector();
        if (moveVector.lengthSquared() != 0.0F)
            moveVector = moveVector.scale(0.98F);
        applyInputHelper(moveVector, isJumpKeyDown());
    }

    private void applyInputHelper(Vec2 moveVector, boolean jumping) {
        this.xxa = moveVector.x;
        this.zza = moveVector.y;
        this.jumping = jumping;
        this.setSprinting((MC.options.keySprint.isDown() && this.isForwardKeyDown()) || (this.isForwardKeyDown() && this.isSprinting()));
        this.yBobO = this.yBob();
        this.xBobO = this.xBob();
        this.xBob = this.xBob() + (this.getXRot() - this.xBob()) * 0.5F;
        this.yBob = this.yBob() + (this.getYRot() - this.yBob()) * 0.5F;
    }

    private ClientInput getInput() {
        return hasInput.getAsBoolean() ? keyboardInput : dummyInput;
    }

    public boolean isForwardKeyDown() {
        return getInput().keyPresses.forward();
    }

    public boolean isBackwardKeyDown() {
        return getInput().keyPresses.backward();
    }

    public boolean isRightKeyDown() {
        return getInput().keyPresses.right();
    }

    public boolean isLeftKeyDown() {
        return getInput().keyPresses.left();
    }

    public boolean isJumpKeyDown() {
        return getInput().keyPresses.jump();
    }

    public boolean isSneakKeyDown() {
        return getInput().keyPresses.shift();
    }

    public float xBob() {
        return xBob;
    }

    public float yBob() {
        return yBob;
    }

    public float xBobO() {
        return xBobO;
    }

    public float yBobO() {
        return yBobO;
    }
}
