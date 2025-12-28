package org.clockworx.hyperconomy.integration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import org.clockworx.hyperconomy.HyperConomy;
import org.clockworx.hyperconomy.currency.CurrencyManager;
import org.clockworx.hyperconomy.currency.CurrencyProvider;

/**
 * Integration handler for Shopkeepers plugin.
 * 
 * <p>This class handles integration between HyperConomy and Shopkeepers,
 * ensuring that Shopkeepers can use HyperConomy's economy system via ServiceIO
 * and that currency items (like gold nuggets) are properly configured.
 * 
 * <p>Shopkeepers integration works automatically through ServiceIO's service
 * registration system. This class primarily handles currency item configuration
 * and provides helper methods for Shopkeepers compatibility.
 */
public class ShopkeepersIntegration {
    
    private final HyperConomy hc;
    private final CurrencyManager currencyManager;
    private boolean shopkeepersInstalled;
    
    /**
     * Creates a new ShopkeepersIntegration instance.
     * 
     * @param hc The HyperConomy instance
     */
    public ShopkeepersIntegration(HyperConomy hc) {
        this.hc = hc;
        this.currencyManager = hc.getCurrencyManager();
        checkShopkeepersInstallation();
    }
    
    /**
     * Checks if Shopkeepers plugin is installed.
     */
    private void checkShopkeepersInstallation() {
        Plugin shopkeepers = Bukkit.getServer().getPluginManager().getPlugin("Shopkeepers");
        shopkeepersInstalled = (shopkeepers != null && shopkeepers.isEnabled());
        
        if (shopkeepersInstalled) {
            hc.getDebugMode().syncDebugConsoleMessage(
                "[HyperConomy] Shopkeepers plugin detected. Integration enabled.");
            
            // Log currency information for Shopkeepers configuration
            CurrencyProvider provider = currencyManager.getCurrencyProvider();
            if (provider != null && provider.isItemBased()) {
                hc.getDebugMode().syncDebugConsoleMessage(
                    "[HyperConomy] Currency item for Shopkeepers: " + 
                    provider.getCurrencyMaterial().name() + " (" + 
                    provider.getCurrencyName() + ")");
            }
        }
    }
    
    /**
     * Gets whether Shopkeepers is installed and enabled.
     * 
     * @return true if Shopkeepers is installed, false otherwise
     */
    public boolean isShopkeepersInstalled() {
        return shopkeepersInstalled;
    }
    
    /**
     * Gets the currency Material for Shopkeepers configuration.
     * 
     * @return The Material used for currency, or null if balance-based
     */
    public org.bukkit.Material getCurrencyMaterial() {
        CurrencyProvider provider = currencyManager.getCurrencyProvider();
        if (provider != null && provider.isItemBased()) {
            return provider.getCurrencyMaterial();
        }
        return null;
    }
    
    /**
     * Gets the currency display name for Shopkeepers.
     * 
     * @return The currency display name
     */
    public String getCurrencyName() {
        CurrencyProvider provider = currencyManager.getCurrencyProvider();
        if (provider != null) {
            return provider.getCurrencyName();
        }
        return "Money";
    }
    
    /**
     * Reloads the integration (useful when configuration changes).
     */
    public void reload() {
        checkShopkeepersInstallation();
    }
}

