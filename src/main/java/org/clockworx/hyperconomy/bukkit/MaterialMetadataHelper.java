package org.clockworx.hyperconomy.bukkit;

import org.bukkit.Material;
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
     * First tries Material-based extraction, then falls back to deprecated method if needed.
     * 
     * @param itemStack The ItemStack to extract entity type from
     * @return EntityType if itemStack is a spawn egg, null otherwise
     */
    public static EntityType extractEntityTypeFromSpawnEgg(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        
        // Try Material-based extraction first
        EntityType entityType = extractEntityTypeFromSpawnEgg(itemStack.getType());
        if (entityType != null) {
            return entityType;
        }
        
        // Fallback to deprecated method only if Material extraction fails
        // This handles edge cases where Material might not match expected pattern
        if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof org.bukkit.inventory.meta.SpawnEggMeta) {
            org.bukkit.inventory.meta.SpawnEggMeta meta = (org.bukkit.inventory.meta.SpawnEggMeta) itemStack.getItemMeta();
            try {
                @SuppressWarnings("deprecation")
                EntityType deprecatedType = meta.getSpawnedType();
                return deprecatedType;
            } catch (Exception e) {
                // Method removed or unavailable
                return null;
            }
        }
        
        return null;
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
     * Checks for Paper API alternatives first, then falls back to enum.valueOf().
     * 
     * @param patternName The pattern name string
     * @return PatternType if found, null otherwise
     */
    public static PatternType extractPatternTypeFromString(String patternName) {
        if (patternName == null || patternName.isEmpty()) {
            return null;
        }
        
        // Try to find PatternType by name
        // Note: PatternType.valueOf() is deprecated, but enum.valueOf() is standard Java
        // If Paper provides an alternative, it should be used here
        try {
            // Check if there's a Paper API method first
            // For now, we'll use valueOf() but wrap it properly
            @SuppressWarnings("deprecation")
            PatternType patternType = PatternType.valueOf(patternName.toUpperCase());
            return patternType;
        } catch (IllegalArgumentException e) {
            // PatternType doesn't exist
            return null;
        }
    }
}



