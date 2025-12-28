package org.clockworx.hyperconomy.currency;

/**
 * Enum representing the different types of currency systems supported by HyperConomy.
 * 
 * <ul>
 *   <li>BALANCE - Traditional balance-based economy (no physical items)</li>
 *   <li>ITEM - Item-based currency using Minecraft items (e.g., gold nuggets)</li>
 *   <li>CUSTOM_COIN - Custom coin items with resource pack support (future implementation)</li>
 * </ul>
 */
public enum CurrencyType {
    /**
     * Balance-based currency system where money exists only as a numeric value.
     * This is the traditional economy system used by most plugins.
     */
    BALANCE,
    
    /**
     * Item-based currency system where money exists as physical items in player inventories.
     * Default implementation uses gold nuggets, but can be configured to use any Material.
     */
    ITEM,
    
    /**
     * Custom coin currency system with resource pack support.
     * This allows for custom textures and models via resource packs.
     * Future implementation - not yet available.
     */
    CUSTOM_COIN
}

