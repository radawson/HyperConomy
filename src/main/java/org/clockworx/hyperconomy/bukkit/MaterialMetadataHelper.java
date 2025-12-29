package org.clockworx.hyperconomy.bukkit;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * Helper class for extracting metadata from Material and ItemStack objects.
 * Uses modern Paper API patterns to avoid deprecated methods.
 */
public class MaterialMetadataHelper {

    /**
     * Extracts EntityType from a spawn egg Material by parsing the Material name.
     * Modern Paper uses distinct Material types for each spawn egg (e.g., ZOMBIE_SPAWN_EGG).
     * 
     * @param material The Material to extract entity type from
     * @return EntityType if material is a spawn egg, null otherwise
     */
    public static EntityType extractEntityTypeFromSpawnEgg(Material material) {
        if (material == null) {
            return null;
        }
        
        String materialName = material.name();
        
        // Check if it's a spawn egg material
        if (!materialName.endsWith("_SPAWN_EGG")) {
            return null;
        }
        
        // Extract entity type name (e.g., "ZOMBIE_SPAWN_EGG" -> "ZOMBIE")
        String entityTypeName = materialName.substring(0, materialName.length() - "_SPAWN_EGG".length());
        
        try {
            return EntityType.valueOf(entityTypeName);
        } catch (IllegalArgumentException e) {
            // EntityType doesn't exist for this spawn egg
            return null;
        }
    }

    /**
     * Extracts EntityType from a spawn egg ItemStack.
     * Uses Material-based extraction (modern Paper uses distinct Material types for each spawn egg).
     * 
     * @param itemStack The ItemStack to extract entity type from
     * @return EntityType if itemStack is a spawn egg, null otherwise
     */
    public static EntityType extractEntityTypeFromSpawnEgg(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        
        // Modern Paper uses distinct Material types for each spawn egg
        // Extract entity type from Material name (e.g., ZOMBIE_SPAWN_EGG -> ZOMBIE)
        return extractEntityTypeFromSpawnEgg(itemStack.getType());
    }

    /**
     * Gets the spawn egg Material for a given EntityType.
     * Converts EntityType to corresponding spawn egg Material (e.g., ZOMBIE -> ZOMBIE_SPAWN_EGG).
     * 
     * @param entityType The EntityType to get spawn egg Material for
     * @return Material for the spawn egg, or null if no spawn egg exists for this entity
     */
    public static Material getSpawnEggMaterial(EntityType entityType) {
        if (entityType == null) {
            return null;
        }
        
        String materialName = entityType.name() + "_SPAWN_EGG";
        
        try {
            Material material = Material.valueOf(materialName);
            // Verify it's actually a spawn egg material
            if (isSpawnEggMaterial(material)) {
                return material;
            }
        } catch (IllegalArgumentException e) {
            // Material doesn't exist for this entity type
        }
        
        return null;
    }

    /**
     * Checks if a Material is a spawn egg type.
     * 
     * @param material The Material to check
     * @return true if material is a spawn egg, false otherwise
     */
    public static boolean isSpawnEggMaterial(Material material) {
        if (material == null) {
            return false;
        }
        
        return material.name().endsWith("_SPAWN_EGG");
    }

    /**
     * Extracts PatternType from a string.
     * Uses Paper Registry API (1.21+) with fallback to deprecated enum methods.
     * Note: Both Registry.BANNER_PATTERN and PatternType enum methods are deprecated in 1.21+,
     * but they remain functional until a replacement API is provided.
     * 
     * @param patternName The pattern name string
     * @return PatternType if found, null otherwise
     */
    @SuppressWarnings("deprecation")
    public static PatternType extractPatternTypeFromString(String patternName) {
        if (patternName == null || patternName.isEmpty()) {
            return null;
        }
        
        // Try Registry API first (Paper 1.21+) - deprecated but still functional
        try {
            // Try with minecraft: namespace first
            NamespacedKey key = NamespacedKey.minecraft(patternName.toLowerCase());
            PatternType patternType = Registry.BANNER_PATTERN.get(key);
            if (patternType != null) {
                return patternType;
            }
        } catch (Exception e) {
            // Registry lookup failed, try fallback
        }
        
        // Fallback: Try with explicit minecraft: prefix if not already present
        try {
            String lowerPatternName = patternName.toLowerCase();
            if (!lowerPatternName.contains(":")) {
                NamespacedKey key = NamespacedKey.minecraft(lowerPatternName);
                PatternType patternType = Registry.BANNER_PATTERN.get(key);
                if (patternType != null) {
                    return patternType;
                }
            }
        } catch (Exception e) {
            // Fallback failed
        }
        
        // Last resort: Use deprecated enum valueOf() method
        // This handles cases where Registry might not have the pattern
        try {
            PatternType patternType = PatternType.valueOf(patternName.toUpperCase());
            return patternType;
        } catch (IllegalArgumentException e) {
            // PatternType not found
            return null;
        }
    }
}



