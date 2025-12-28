package org.clockworx.hyperconomy.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Helper class for Paper Component API operations.
 * Provides utilities for converting between legacy color codes and modern Component API.
 */
public class ComponentHelper {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    /**
     * Converts legacy color codes (&a, &l, etc.) to Component.
     * 
     * @param legacyText Text with legacy color codes
     * @return Component representing the formatted text
     */
    public static Component legacyToComponent(String legacyText) {
        if (legacyText == null) {
            return Component.empty();
        }
        return LEGACY_SERIALIZER.deserialize(legacyText);
    }

    /**
     * Converts Component back to legacy format if needed.
     * 
     * @param component Component to convert
     * @return Legacy color code string
     */
    public static String componentToLegacy(Component component) {
        if (component == null) {
            return "";
        }
        return LEGACY_SERIALIZER.serialize(component);
    }

    /**
     * Strips color codes from text using Component API.
     * 
     * @param text Text with color codes
     * @return Plain text without color codes
     */
    public static String stripColor(String text) {
        if (text == null) {
            return "";
        }
        // Convert to Component and extract plain text
        Component component = legacyToComponent(text);
        // Use legacy serializer to get plain text (without formatting)
        return LEGACY_SERIALIZER.serialize(component).replaceAll("§[0-9a-fk-or]", "");
    }

    /**
     * Applies legacy color codes to a message using Component API.
     * This replaces the old ChatColor-based applyColor method.
     * 
     * @param message Message with legacy color codes (&a, &l, etc.)
     * @return String with color codes applied (for backward compatibility with String-based APIs)
     */
    public static String applyColor(String message) {
        if (message == null) {
            return "";
        }
        // Component API handles legacy color codes automatically
        // Convert to Component and back to ensure proper formatting
        Component component = legacyToComponent(message);
        return componentToLegacy(component);
    }

    /**
     * Applies legacy color codes and returns a Component.
     * Use this when working with Component-based APIs.
     * 
     * @param message Message with legacy color codes
     * @return Component with formatting applied
     */
    public static Component applyColorComponent(String message) {
        if (message == null) {
            return Component.empty();
        }
        return legacyToComponent(message);
    }
}

