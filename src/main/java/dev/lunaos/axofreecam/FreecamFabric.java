package dev.lunaos.axofreecam;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import dev.lunaos.axofreecam.Freecam;
import dev.lunaos.axofreecam.config.ModBindings;
import dev.lunaos.axofreecam.config.ModConfig;

public class FreecamFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModConfig.setup();
        ModBindings.forEach(KeyMappingHelper::registerKeyMapping);
        ClientTickEvents.START_CLIENT_TICK.register(Freecam::preTick);
        ClientTickEvents.END_CLIENT_TICK.register(Freecam::postTick);
    }
}
