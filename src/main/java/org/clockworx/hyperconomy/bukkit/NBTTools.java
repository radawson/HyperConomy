package org.clockworx.hyperconomy.bukkit;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * NBTTools provides methods for storing and retrieving custom data on ItemStacks.
 * 
 * <p>This class has been modernized for Minecraft 1.20.5+ which replaced the old
 * NBT tag system with Data Components. The implementation now uses Bukkit's
 * PersistentDataContainer API which is stable across all modern Minecraft versions.
 * 
 * <p>The PersistentDataContainer API stores custom data in a way that:
 * <ul>
 *   <li>Survives server restarts and item serialization</li>
 *   <li>Is properly namespaced to avoid conflicts with other plugins</li>
 *   <li>Works consistently across all supported Minecraft versions</li>
 *   <li>Does not require NMS reflection or version-specific code</li>
 * </ul>
 * 
 * @author RegalOwl, ClockWorX (original), modernized for Paper 1.20.5+
 */
public class NBTTools {

	/** The plugin instance used for creating namespaced keys. */
	private Plugin plugin;
	
	/** Flag indicating whether the tools loaded successfully. */
	private boolean loadedSuccessfully = false;

	/**
	 * Constructs a new NBTTools instance.
	 * 
	 * <p>This constructor initializes the PersistentDataContainer-based
	 * implementation which works on Minecraft 1.14+ and is required for 1.20.5+.
	 */
	public NBTTools() {
		try {
			// Get the plugin instance for creating namespaced keys
			this.plugin = org.bukkit.Bukkit.getPluginManager().getPlugin("HyperConomy");
			if (this.plugin == null) {
				// Plugin not yet loaded, try to get it later
				// This can happen during early initialization
				System.out.println("[HyperConomy] NBTTools: Plugin not yet available, will initialize lazily.");
			}
			loadedSuccessfully = true;
		} catch (Exception e) {
			e.printStackTrace();
			loadedSuccessfully = false;
		}
	}

	/**
	 * Ensures the plugin reference is available.
	 * 
	 * @return true if plugin is available, false otherwise
	 */
	private boolean ensurePlugin() {
		if (plugin == null) {
			plugin = org.bukkit.Bukkit.getPluginManager().getPlugin("HyperConomy");
		}
		return plugin != null;
	}

	/**
	 * Creates a namespaced key for the given string key.
	 * 
	 * @param key the key name
	 * @return the namespaced key, or null if plugin is not available
	 */
	private NamespacedKey getKey(String key) {
		if (!ensurePlugin()) return null;
		return new NamespacedKey(plugin, key);
	}

	/**
	 * Checks if the NBTTools loaded successfully.
	 * 
	 * @return true if loaded successfully, false otherwise
	 */
	public boolean loadedSuccessfully() {
		return loadedSuccessfully;
	}

	/**
	 * Checks if an ItemStack has a custom data value for the specified key.
	 * 
	 * @param itemStack the item to check
	 * @param key the key to look for
	 * @return true if the key exists, false otherwise
	 */
	public boolean hasKey(ItemStack itemStack, String key) {
		if (itemStack == null || !itemStack.hasItemMeta()) return false;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return false;
		
		PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
		// Check for any of our supported types
		return pdc.has(nsKey, PersistentDataType.STRING) ||
		       pdc.has(nsKey, PersistentDataType.INTEGER) ||
		       pdc.has(nsKey, PersistentDataType.DOUBLE) ||
		       pdc.has(nsKey, PersistentDataType.BYTE);
	}

	/**
	 * Gets all custom data keys stored on an ItemStack.
	 * 
	 * <p>Note: This only returns keys that were set by this plugin (HyperConomy namespace).
	 * 
	 * @param itemStack the item to check
	 * @return list of key names (without namespace prefix), empty list if none
	 */
	public ArrayList<String> getNMSKeys(ItemStack itemStack) {
		ArrayList<String> keys = new ArrayList<>();
		if (itemStack == null || !itemStack.hasItemMeta()) return keys;
		if (!ensurePlugin()) return keys;
		
		try {
			PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
			Set<NamespacedKey> pdcKeys = pdc.getKeys();
			String namespace = plugin.getName().toLowerCase();
			
			for (NamespacedKey nsKey : pdcKeys) {
				// Only include keys from our namespace
				if (nsKey.getNamespace().equalsIgnoreCase(namespace)) {
					keys.add(nsKey.getKey());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keys;
	}

	/**
	 * Gets the NMS ItemStack representation.
	 * 
	 * <p>Note: This method is deprecated for 1.20.5+ as direct NMS access is no longer
	 * needed. It now returns the Bukkit ItemStack directly for API compatibility.
	 * 
	 * @param itemStack the Bukkit item stack
	 * @return the same item stack (NMS conversion no longer needed)
	 * @deprecated Use Bukkit API directly instead of NMS access
	 */
	@Deprecated
	public Object getNMSItemStack(ItemStack itemStack) {
		// For API compatibility, just return the Bukkit ItemStack
		// Modern Paper plugins should not need direct NMS access
		return itemStack;
	}

	/**
	 * Gets a Bukkit ItemStack from an NMS ItemStack.
	 * 
	 * <p>Note: This method is deprecated for 1.20.5+ as direct NMS access is no longer
	 * needed. It now expects a Bukkit ItemStack and returns it directly.
	 * 
	 * @param nmsItemStack the item stack object (should be Bukkit ItemStack)
	 * @return the Bukkit item stack
	 * @deprecated Use Bukkit API directly instead of NMS access
	 */
	@Deprecated
	public ItemStack getBukkitItemStack(Object nmsItemStack) {
		if (nmsItemStack instanceof ItemStack) {
			return (ItemStack) nmsItemStack;
		}
		return null;
	}

	/**
	 * Gets the NBT tag from an NMS ItemStack.
	 * 
	 * <p>Note: This method is deprecated. In Minecraft 1.20.5+, items use Data Components
	 * instead of NBT tags. Use PersistentDataContainer methods instead.
	 * 
	 * @param nmsItemStack the NMS item stack
	 * @return null (NBT tags no longer exist in 1.20.5+)
	 * @deprecated NBT tags replaced by Data Components in 1.20.5+
	 */
	@Deprecated
	public Object getNBTTag(Object nmsItemStack) {
		// NBT tags no longer exist in 1.20.5+
		// Return null to indicate no tag available
		return null;
	}

	/**
	 * Sets the NBT tag on an NMS ItemStack.
	 * 
	 * <p>Note: This method is deprecated. In Minecraft 1.20.5+, items use Data Components
	 * instead of NBT tags. Use setString/setInt/etc methods instead.
	 * 
	 * @param nmsItemStack the NMS item stack
	 * @param nbtTag the NBT tag to set
	 * @deprecated NBT tags replaced by Data Components in 1.20.5+
	 */
	@Deprecated
	public void setNBTTag(Object nmsItemStack, Object nbtTag) {
		// No-op: NBT tags no longer exist in 1.20.5+
	}

	/**
	 * Generates a new NBT tag compound.
	 * 
	 * <p>Note: This method is deprecated. In Minecraft 1.20.5+, items use Data Components
	 * instead of NBT tags.
	 * 
	 * @return null (NBT tags no longer exist in 1.20.5+)
	 * @deprecated NBT tags replaced by Data Components in 1.20.5+
	 */
	@Deprecated
	public Object generateNBTTag() {
		// NBT tags no longer exist in 1.20.5+
		return null;
	}

	// ============================================
	// Compound Tag Methods (deprecated, no-op)
	// ============================================

	/**
	 * @deprecated Use PersistentDataContainer methods instead
	 */
	@Deprecated
	public Object getNBTTagCompoundCompound(Object nbtTag, String key) {
		return null;
	}

	/**
	 * @deprecated Use PersistentDataContainer methods instead
	 */
	@Deprecated
	public void setNBTTagCompoundCompound(Object nbtTag, String key, Object nestedTag) {
		// No-op
	}

	/**
	 * @deprecated Use getString(ItemStack, String) instead
	 */
	@Deprecated
	public String getNBTTagCompoundString(Object nbtTag, String key) {
		return null;
	}

	/**
	 * @deprecated Use setString(ItemStack, String, String) instead
	 */
	@Deprecated
	public void setNBTTagCompoundString(Object nbtTag, String key, String s) {
		// No-op
	}

	/**
	 * @deprecated Use getInt(ItemStack, String) instead
	 */
	@Deprecated
	public Integer getNBTTagCompoundInt(Object nbtTag, String key) {
		return null;
	}

	/**
	 * @deprecated Use setInt(ItemStack, String, Integer) instead
	 */
	@Deprecated
	public void setNBTTagCompoundInt(Object nbtTag, String key, Integer i) {
		// No-op
	}

	/**
	 * @deprecated Use getDouble(ItemStack, String) instead
	 */
	@Deprecated
	public Double getNBTTagCompoundDouble(Object nbtTag, String key) {
		return null;
	}

	/**
	 * @deprecated Use setDouble(ItemStack, String, Double) instead
	 */
	@Deprecated
	public void setNBTTagCompoundDouble(Object nbtTag, String key, Double d) {
		// No-op
	}

	/**
	 * @deprecated Use getBoolean(ItemStack, String) instead
	 */
	@Deprecated
	public Boolean getNBTTagCompoundBoolean(Object nbtTag, String key) {
		return null;
	}

	/**
	 * @deprecated Use setBoolean(ItemStack, String, Boolean) instead
	 */
	@Deprecated
	public void setNBTTagCompoundBoolean(Object nbtTag, String key, Boolean b) {
		// No-op
	}

	// ============================================
	// Modern PersistentDataContainer Methods
	// ============================================

	/**
	 * Gets a nested compound value from an ItemStack.
	 * 
	 * <p>Note: Nested compounds are not directly supported by PersistentDataContainer.
	 * This method returns null as a placeholder for API compatibility.
	 * 
	 * @param itemStack the item to read from
	 * @param key the key to look up
	 * @return null (nested compounds not supported)
	 * @deprecated Nested compounds not supported in modern API
	 */
	@Deprecated
	public Object getCompound(ItemStack itemStack, String key) {
		// Nested compounds not directly supported by PersistentDataContainer
		return null;
	}

	/**
	 * Sets a nested compound value on an ItemStack.
	 * 
	 * <p>Note: Nested compounds are not directly supported by PersistentDataContainer.
	 * This method returns the original item unchanged.
	 * 
	 * @param itemStack the item to modify
	 * @param key the key to set
	 * @param c the compound to set
	 * @return the original item unchanged
	 * @deprecated Nested compounds not supported in modern API
	 */
	@Deprecated
	public ItemStack setCompound(ItemStack itemStack, String key, Object c) {
		return itemStack;
	}

	/**
	 * Gets a string value stored on an ItemStack.
	 * 
	 * @param itemStack the item to read from
	 * @param key the key to look up
	 * @return the string value, or null if not found
	 */
	public String getString(ItemStack itemStack, String key) {
		if (itemStack == null || !itemStack.hasItemMeta()) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return null;
		
		try {
			PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
			return pdc.get(nsKey, PersistentDataType.STRING);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets a string value on an ItemStack.
	 * 
	 * @param itemStack the item to modify
	 * @param key the key to set
	 * @param s the string value to store
	 * @return the modified item, or null if operation failed
	 */
	public ItemStack setString(ItemStack itemStack, String key, String s) {
		if (itemStack == null) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return itemStack;
		
		try {
			ItemMeta meta = itemStack.getItemMeta();
			if (meta == null) return itemStack;
			
			PersistentDataContainer pdc = meta.getPersistentDataContainer();
			pdc.set(nsKey, PersistentDataType.STRING, s);
			itemStack.setItemMeta(meta);
			return itemStack;
		} catch (Exception e) {
			e.printStackTrace();
			return itemStack;
		}
	}

	/**
	 * Gets an integer value stored on an ItemStack.
	 * 
	 * @param itemStack the item to read from
	 * @param key the key to look up
	 * @return the integer value, or null if not found
	 */
	public Integer getInt(ItemStack itemStack, String key) {
		if (itemStack == null || !itemStack.hasItemMeta()) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return null;
		
		try {
			PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
			return pdc.get(nsKey, PersistentDataType.INTEGER);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets an integer value on an ItemStack.
	 * 
	 * @param itemStack the item to modify
	 * @param key the key to set
	 * @param i the integer value to store
	 * @return the modified item, or null if operation failed
	 */
	public ItemStack setInt(ItemStack itemStack, String key, Integer i) {
		if (itemStack == null) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return itemStack;
		
		try {
			ItemMeta meta = itemStack.getItemMeta();
			if (meta == null) return itemStack;
			
			PersistentDataContainer pdc = meta.getPersistentDataContainer();
			pdc.set(nsKey, PersistentDataType.INTEGER, i);
			itemStack.setItemMeta(meta);
			return itemStack;
		} catch (Exception e) {
			e.printStackTrace();
			return itemStack;
		}
	}

	/**
	 * Gets a double value stored on an ItemStack.
	 * 
	 * @param itemStack the item to read from
	 * @param key the key to look up
	 * @return the double value, or null if not found
	 */
	public Double getDouble(ItemStack itemStack, String key) {
		if (itemStack == null || !itemStack.hasItemMeta()) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return null;
		
		try {
			PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
			return pdc.get(nsKey, PersistentDataType.DOUBLE);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets a double value on an ItemStack.
	 * 
	 * @param itemStack the item to modify
	 * @param key the key to set
	 * @param d the double value to store
	 * @return the modified item, or null if operation failed
	 */
	public ItemStack setDouble(ItemStack itemStack, String key, Double d) {
		if (itemStack == null) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return itemStack;
		
		try {
			ItemMeta meta = itemStack.getItemMeta();
			if (meta == null) return itemStack;
			
			PersistentDataContainer pdc = meta.getPersistentDataContainer();
			pdc.set(nsKey, PersistentDataType.DOUBLE, d);
			itemStack.setItemMeta(meta);
			return itemStack;
		} catch (Exception e) {
			e.printStackTrace();
			return itemStack;
		}
	}

	/**
	 * Gets a boolean value stored on an ItemStack.
	 * 
	 * <p>Booleans are stored as bytes (0 = false, 1 = true) in PersistentDataContainer.
	 * 
	 * @param itemStack the item to read from
	 * @param key the key to look up
	 * @return the boolean value, or null if not found
	 */
	public Boolean getBoolean(ItemStack itemStack, String key) {
		if (itemStack == null || !itemStack.hasItemMeta()) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return null;
		
		try {
			PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
			Byte value = pdc.get(nsKey, PersistentDataType.BYTE);
			if (value == null) return null;
			return value != 0;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets a boolean value on an ItemStack.
	 * 
	 * <p>Booleans are stored as bytes (0 = false, 1 = true) in PersistentDataContainer.
	 * 
	 * @param itemStack the item to modify
	 * @param key the key to set
	 * @param b the boolean value to store
	 * @return the modified item, or null if operation failed
	 */
	public ItemStack setBoolean(ItemStack itemStack, String key, Boolean b) {
		if (itemStack == null) return null;
		NamespacedKey nsKey = getKey(key);
		if (nsKey == null) return itemStack;
		
		try {
			ItemMeta meta = itemStack.getItemMeta();
			if (meta == null) return itemStack;
			
			PersistentDataContainer pdc = meta.getPersistentDataContainer();
			pdc.set(nsKey, PersistentDataType.BYTE, (byte) (b ? 1 : 0));
			itemStack.setItemMeta(meta);
			return itemStack;
		} catch (Exception e) {
			e.printStackTrace();
			return itemStack;
		}
	}

	/**
	 * Gets the mob entity type from a spawn egg.
	 * 
	 * <p>In modern Minecraft, spawn egg types are determined by the Material itself
	 * (e.g., ZOMBIE_SPAWN_EGG, CREEPER_SPAWN_EGG) rather than NBT data.
	 * 
	 * @param itemStack the spawn egg item
	 * @return the entity type ID (e.g., "minecraft:zombie"), or null if not a spawn egg
	 */
	public String getMobEggType(ItemStack itemStack) {
		if (itemStack == null) return null;
		
		Material material = itemStack.getType();
		String materialName = material.name();
		
		// Check if it's a spawn egg
		if (!materialName.endsWith("_SPAWN_EGG")) {
			return null;
		}
		
		// Extract entity type from material name (e.g., ZOMBIE_SPAWN_EGG -> zombie)
		String entityType = materialName.replace("_SPAWN_EGG", "").toLowerCase();
		return "minecraft:" + entityType;
	}

	/**
	 * Sets the mob entity type for a spawn egg.
	 * 
	 * <p>In modern Minecraft, spawn egg types are determined by the Material itself.
	 * This method returns a new spawn egg of the specified type.
	 * 
	 * @param itemStack the original spawn egg item
	 * @param type the entity type ID (e.g., "minecraft:zombie" or just "zombie")
	 * @return a new spawn egg of the specified type, or the original if type is invalid
	 */
	public ItemStack setMobEggType(ItemStack itemStack, String type) {
		if (itemStack == null || type == null) return itemStack;
		
		// Parse entity type (remove minecraft: prefix if present)
		String entityType = type.replace("minecraft:", "").toUpperCase();
		String materialName = entityType + "_SPAWN_EGG";
		
		try {
			Material newMaterial = Material.valueOf(materialName);
			ItemStack newStack = new ItemStack(newMaterial, itemStack.getAmount());
			
			// Copy over any item meta
			if (itemStack.hasItemMeta()) {
				newStack.setItemMeta(itemStack.getItemMeta());
			}
			
			return newStack;
		} catch (IllegalArgumentException e) {
			// Invalid entity type
			return itemStack;
		}
	}

	/**
	 * Gets the display name of an ItemStack.
	 * 
	 * <p>This method uses the modern Bukkit/Paper API to retrieve item names.
	 * It returns the custom display name if set, otherwise the translatable item name.
	 * 
	 * @param itemStack the item to get the name from
	 * @return the item's display name, or null if the item is null/air
	 */
	public String getName(ItemStack itemStack) {
		if (itemStack == null || itemStack.getType() == Material.AIR) {
			return null;
		}
		
		try {
			// Check if item has a custom display name
			if (itemStack.hasItemMeta()) {
				ItemMeta meta = itemStack.getItemMeta();
				if (meta != null && meta.hasDisplayName()) {
					// Return the plain text of the custom display name
					net.kyori.adventure.text.Component displayName = meta.displayName();
					if (displayName != null) {
						return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
							.plainText().serialize(displayName);
					}
				}
			}
			
			// Fall back to material name (formatted nicely)
			// Convert DIAMOND_SWORD to "Diamond Sword"
			String materialName = itemStack.getType().name();
			StringBuilder result = new StringBuilder();
			boolean capitalizeNext = true;
			
			for (char c : materialName.toCharArray()) {
				if (c == '_') {
					result.append(' ');
					capitalizeNext = true;
				} else if (capitalizeNext) {
					result.append(Character.toUpperCase(c));
					capitalizeNext = false;
				} else {
					result.append(Character.toLowerCase(c));
				}
			}
			
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
