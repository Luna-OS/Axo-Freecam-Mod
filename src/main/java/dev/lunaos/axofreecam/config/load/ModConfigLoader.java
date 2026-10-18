package dev.lunaos.axofreecam.config.load;

import dev.lunaos.axofreecam.config.model.ModConfigDTO;

import java.nio.file.Path;

public class ModConfigLoader extends MigratingConfigLoader<ModConfigDTO> {

    public ModConfigLoader(ConfigSerializer<?> serializer, String name, Path configDir) {
        super(
            new BasicConfigLoader<>(serializer, ModConfigDTO.class, configDir.resolve(name + ".json")),
            new BasicConfigLoader<>(serializer, ModConfigDTO.class, configDir.resolve(name + ".json5"))
        );
    }
}
