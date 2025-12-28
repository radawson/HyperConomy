package org.clockworx.hyperconomy.currency;

import org.clockworx.hyperconomy.HyperConomy;
import regalowl.simpledatalib.file.FileConfiguration;

/**
 * Manager class for currency providers in HyperConomy.
 * 
 * <p>This class loads and provides the appropriate currency provider based on
 * configuration settings. It supports multiple currency types and can be
 * extended to support new currency systems in the future.
 */
public class CurrencyManager {
    
    private final HyperConomy hc;
    private CurrencyProvider currencyProvider;
    
    /**
     * Creates a new CurrencyManager and loads the configured currency provider.
     * 
     * @param hc The HyperConomy instance
     */
    public CurrencyManager(HyperConomy hc) {
        this.hc = hc;
        loadCurrencyProvider();
    }
    
    /**
     * Loads the currency provider based on configuration settings.
     */
    private void loadCurrencyProvider() {
        FileConfiguration config = hc.getConf();
        String currencyTypeStr = config.getString("currency.type", "BALANCE");
        
        try {
            CurrencyType currencyType = CurrencyType.valueOf(currencyTypeStr.toUpperCase());
            
            switch (currencyType) {
                case ITEM:
                    currencyProvider = new ItemCurrencyProvider(hc);
                    hc.getDebugMode().syncDebugConsoleMessage(
                        "[HyperConomy] Currency system: Item-based (" + 
                        currencyProvider.getCurrencyName() + ")");
                    break;
                    
                case CUSTOM_COIN:
                    // Future implementation for resource pack coins
                    hc.getDebugMode().syncDebugConsoleMessage(
                        "[HyperConomy] Custom coin currency not yet implemented, defaulting to item-based");
                    currencyProvider = new ItemCurrencyProvider(hc);
                    break;
                    
                case BALANCE:
                default:
                    // Balance-based currency (no provider needed, uses traditional balance system)
                    currencyProvider = null;
                    hc.getDebugMode().syncDebugConsoleMessage(
                        "[HyperConomy] Currency system: Balance-based");
                    break;
            }
        } catch (IllegalArgumentException e) {
            hc.getDebugMode().syncDebugConsoleMessage(
                "Invalid currency type '" + currencyTypeStr + "', defaulting to BALANCE");
            currencyProvider = null;
        }
    }
    
    /**
     * Gets the current currency provider.
     * 
     * @return The CurrencyProvider instance, or null if using balance-based currency
     */
    public CurrencyProvider getCurrencyProvider() {
        return currencyProvider;
    }
    
    /**
     * Checks if the currency system is item-based.
     * 
     * @return true if using item-based currency, false if balance-based
     */
    public boolean isItemBased() {
        return currencyProvider != null && currencyProvider.isItemBased();
    }
    
    /**
     * Reloads the currency provider from configuration.
     * Useful when configuration changes are made.
     */
    public void reload() {
        loadCurrencyProvider();
    }
}

