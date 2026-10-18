package dev.lunaos.axofreecam.config.gui;

import dev.lunaos.axofreecam.Freecam;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

/**
 * Luna-OS colour palette (moonlight on a deep purple night sky).
 */
public final class LunaTheme {

    /** Pale moonlight, used for the moon glyph. */
    public static final int MOON = 0xEDE7F6;
    /** Soft lilac, the main accent colour. */
    public static final int LILAC = 0xB39DDB;
    /** Bright violet, used for headings. */
    public static final int VIOLET = 0x9575CD;

    /** Tiled night-sky background shown when the settings are opened outside a world. */
    public static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(Freecam.MOD_ID, "textures/gui/luna_background.png");

    private LunaTheme() {}

    /** A screen title prefixed with a crescent moon, e.g. "☾ Axo Freecam Optionen". */
    public static Component title(String translationKey) {
        return Component.literal("☾ ").withColor(MOON)
                .append(Component.translatable(translationKey).withColor(LILAC));
    }

    /**
     * A section heading in the settings screen.
     * <p>
     * Cloth Config restyles the root component (grey/italic/red), so the colour lives on a child
     * component where it survives that restyling.
     */
    public static MutableComponent heading(String translationKey) {
        return Component.empty().append(Component.translatable(translationKey).withColor(VIOLET));
    }
}
