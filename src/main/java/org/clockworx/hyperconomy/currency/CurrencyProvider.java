package org.clockworx.hyperconomy.currency;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Interface for currency providers in HyperConomy.
 * 
 * <p>This interface allows for modular currency systems, supporting:
 * <ul>
 *   <li>Item-based currency (gold nuggets, custom items)</li>
 *   <li>Balance-based currency (traditional economy)</li>
 *   <li>Future: Resource pack-based custom coins</li>
 * </ul>
 * 
 * <p>Implementations should handle currency item creation, validation, and amount extraction.
 */
public interface CurrencyProvider {
    
    /**
     * Gets the Material used for this currency.
     * 
     * @return The Material representing the currency item, or null if balance-based
     */
    Material getCurrencyMaterial();
    
    /**
     * Gets the display name for this currency.
     * 
     * @return The display name (e.g., "Gold Nugget", "Coin")
     */
    String getCurrencyName();
    
    /**
     * Checks if this currency system uses physical items.
     * 
     * @return true if currency uses items, false if balance-based
     */
    boolean isItemBased();
    
    /**
     * Creates a currency item stack with the specified amount.
     * 
     * @param amount The amount of currency items to create
     * @return An ItemStack containing the currency items, or null if balance-based
     */
    ItemStack createCurrencyItem(int amount);
    
    /**
     * Extracts the currency amount from an item stack.
     * 
     * @param stack The ItemStack to extract the amount from
     * @return The amount of currency in the stack, or 0 if not a valid currency item
     */
    int getCurrencyAmount(ItemStack stack);
    
    /**
     * Validates if an ItemStack is a valid currency item.
     * 
     * @param stack The ItemStack to validate
     * @return true if the stack is a valid currency item, false otherwise
     */
    boolean isValidCurrency(ItemStack stack);
    
    /**
     * Gets the currency type for this provider.
     * 
     * @return The CurrencyType enum value
     */
    CurrencyType getCurrencyType();
    
    /**
     * Gets the custom model data value for resource pack support (if applicable).
     * 
     * @return The custom model data value, or null if not used
     */
    Integer getCustomModelData();
}

