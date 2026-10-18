package dev.lunaos.axofreecam;

import net.fabricmc.loader.api.FabricLoader;
import dev.lunaos.axofreecam.ModPlatform;

public class FabricPlatform implements ModPlatform {

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
