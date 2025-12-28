package org.clockworx.hyperconomy.currency;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.clockworx.hyperconomy.HyperConomy;
import regalowl.simpledatalib.file.FileConfiguration;

/**
 * Implementation of CurrencyProvider for item-based currency systems.
 * 
 * <p>This provider supports using any Minecraft Material as currency, with the default
 * being gold nuggets. It can be configured via config.yml to use any item type.
 * 
 * <p>Future support for custom model data is included for resource pack integration.
 */
public class ItemCurrencyProvider implements CurrencyProvider {
    
    private final HyperConomy hc;
    private final Material currencyMaterial;
    private final String currencyName;
    private final Integer customModelData;
    
    /**
     * Creates a new ItemCurrencyProvider with default gold nugget currency.
     * 
     * @param hc The HyperConomy instance
     */
    public ItemCurrencyProvider(HyperConomy hc) {
        this.hc = hc;
        FileConfiguration config = hc.getConf();
        
        // Load currency configuration
        String materialName = config.getString("currency.item.material");
        if (materialName == null) materialName = "GOLD_NUGGET";
        String displayName = config.getString("currency.item.display-name");
        this.currencyName = (displayName != null) ? displayName : "Gold Nugget";
        // Use isSet() instead of contains() for SimpleDataLib FileConfiguration
        this.customModelData = config.isSet("currency.item.custom-model-data") 
            ? config.getInt("currency.item.custom-model-data") 
            : null;
        
        // Parse Material
        Material material;
        try {
            material = Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            hc.getDebugMode().syncDebugConsoleMessage(
                "Invalid currency material '" + materialName + "', defaulting to GOLD_NUGGET");
            material = Material.GOLD_NUGGET;
        }
        this.currencyMaterial = material;
    }
    
    @Override
    public Material getCurrencyMaterial() {
        return currencyMaterial;
    }
    
    @Override
    public String getCurrencyName() {
        return currencyName;
    }
    
    @Override
    public boolean isItemBased() {
        return true;
    }
    
    @Override
    public ItemStack createCurrencyItem(int amount) {
        if (amount <= 0) {
            return null;
        }
        
        ItemStack stack = new ItemStack(currencyMaterial, amount);
        
        // Apply custom model data if configured (for future resource pack support)
        if (customModelData != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(customModelData);
                stack.setItemMeta(meta);
            }
        }
        
        return stack;
    }
    
    @Override
    public int getCurrencyAmount(ItemStack stack) {
        if (!isValidCurrency(stack)) {
            return 0;
        }
        return stack.getAmount();
    }
    
    @Override
    public boolean isValidCurrency(ItemStack stack) {
        if (stack == null || stack.getType() != currencyMaterial) {
            return false;
        }
        
        // If custom model data is configured, validate it matches
        if (customModelData != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                return meta.getCustomModelData() == customModelData;
            }
            // If custom model data is required but item doesn't have it, invalid
            return false;
        }
        
        return true;
    }
    
    @Override
    public CurrencyType getCurrencyType() {
        return CurrencyType.ITEM;
    }
    
    @Override
    public Integer getCustomModelData() {
        return customModelData;
    }
}

