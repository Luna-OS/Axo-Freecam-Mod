package dev.lunaos.axofreecam.config.model;

import net.minecraft.world.level.block.*;
import dev.lunaos.axofreecam.config.model.ModConfigDTO.CollisionConfig.CollisionWhitelist;

import java.util.regex.Pattern;

import static dev.lunaos.axofreecam.config.model.CollisionPredicateBuilder.builder;

class CollisionPredicates {
    private CollisionPredicates() {}

    static final CollisionPredicate COLLIDE = block -> false;
    static final CollisionPredicate IGNORE = block -> true;

    static final CollisionPredicate IGNORE_TRANSPARENT = builder()
            .matching(TransparentBlock.class)
            .matching(IronBarsBlock.class)
            .matching(BarrierBlock.class)
            .build();

    static final CollisionPredicate IGNORE_OPENABLE = builder()
            .matching(FenceGateBlock.class)
            .matching(DoorBlock.class, TrapDoorBlock.class)
            .build();

    static CollisionPredicate whitelist(CollisionWhitelist whitelist) {
        String[] ids = whitelist.ids.stream()
                .map(id -> id.contains(":") ? id : "minecraft:" + id)
                .toArray(String[]::new);

        Pattern[] patterns = whitelist.patterns.stream()
                .map(Pattern::compile)
                .toArray(Pattern[]::new);

        return builder()
                .matching(ids)
                .matching(patterns)
                .build();
    }
}
