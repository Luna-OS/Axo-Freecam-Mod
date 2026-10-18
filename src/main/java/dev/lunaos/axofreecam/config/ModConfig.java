package dev.lunaos.axofreecam.config;

import net.minecraft.world.level.block.Block;
import dev.lunaos.axofreecam.Freecam;
import dev.lunaos.axofreecam.config.controller.BasicConfigController;
import dev.lunaos.axofreecam.config.controller.ConfigController;
import dev.lunaos.axofreecam.config.controller.ConfigControllerRegistry;
import dev.lunaos.axofreecam.config.controller.ModConfigController;
import dev.lunaos.axofreecam.config.load.ConfigLoader;
import dev.lunaos.axofreecam.config.load.ConfigSerializer;
import dev.lunaos.axofreecam.config.load.ModConfigLoader;
import dev.lunaos.axofreecam.config.load.RawJsonPreservingSerializer;
import dev.lunaos.axofreecam.config.model.FlightMode;
import dev.lunaos.axofreecam.config.model.ModConfigDTO;
import dev.lunaos.axofreecam.config.model.ModConfigImpl;
import dev.lunaos.axofreecam.config.model.Perspective;

import java.nio.file.Path;

import static dev.lunaos.axofreecam.Freecam.MC;

public interface ModConfig {

    /**
     * Called once, early during mod initialization.
     * Will load config from disk and perform internal setup.
     */
    static void setup() {
        Path configDir = MC.gameDirectory.toPath().resolve("config");
        ConfigSerializer<?> serializer = new RawJsonPreservingSerializer();
        ConfigLoader<ModConfigDTO> loader = new ModConfigLoader(serializer, Freecam.MOD_ID, configDir);

        ConfigController<ModConfigDTO> dtoController = new BasicConfigController<>(loader, ModConfigDTO::new);
        ConfigControllerRegistry.register(ModConfigDTO.class, dtoController);

        ConfigController<ModConfigImpl> controller = new ModConfigController(dtoController);
        ConfigControllerRegistry.register(ModConfigImpl.class, controller);

        controller.load();
    }

    static ModConfig get() {
        return ConfigControllerRegistry.get(ModConfigImpl.class).getConfig();
    }

    FlightMode getFlightMode();

    double getHorizontalSpeed();

    double getVerticalSpeed();

    boolean ignoreAllCollision();

    boolean shouldCheckInitialCollision();

    boolean ignoreCollisionWith(Block block);

    Perspective getInitialPerspective();

    boolean shouldShowPlayer();

    default boolean shouldHidePlayer() {
        return !shouldShowPlayer();
    }

    boolean shouldShowHand();

    default boolean shouldHideHand() {
        return !shouldShowHand();
    }

    boolean isFullBrightEnabled();

    boolean shouldShowSubmersionFog();

    default boolean shouldHideSubmersionFog() {
        return !shouldShowSubmersionFog();
    }

    boolean shouldDisableOnDamage();

    boolean shouldFreezePlayer();

    boolean shouldPreventInteractions();

    boolean allowInteractionsFromCamera();

    boolean allowInteractionsFromPlayer();

    boolean isRestrictedOnServer(String serverIp);

    boolean shouldNotifyFreecam();

    boolean shouldNotifyTripod();

    boolean shouldOutlinePlayer();
}
