package org.clockworx.hyperconomy.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NBTTools {

	@SuppressWarnings("rawtypes")
	private Class craftItemStackClass;
	private Method craftItemStackAsNMSCopyMethod;
	private Method craftItemStackAsCraftMirrorMethod;
	private Method nmsItemStackGetNameMethod;
	private Method nmsItemStackGetTagMethod;
	private Method nmsItemStackSetTagMethod;
	private Method nbtTagCompoundHasKeyMethod;
	private Method nbtTagCompoundCMethod;
	private Method nbtTagCompoundSetMethod;
	private Method nbtTagCompoundGetCompoundMethod;
	private Method nbtTagCompoundSetStringMethod;
	private Method nbtTagCompoundGetStringMethod;
	private Method nbtTagCompoundSetIntMethod;
	private Method nbtTagCompoundGetIntMethod;
	private Method nbtTagCompoundSetDoubleMethod;
	private Method nbtTagCompoundGetDoubleMethod;
	private Method nbtTagCompoundSetBooleanMethod;
	private Method nbtTagCompoundGetBooleanMethod;
	@SuppressWarnings("rawtypes")
	private Class nbtTagCompound;
	private boolean loadedSuccessfully = false;

	@SuppressWarnings("unchecked")
	public NBTTools() {

		try {
			// Get version from CraftServer class package name
			// Paper's reflection rewriter requires careful version extraction
			String serverPackage = Bukkit.getServer().getClass().getPackage().getName();
			String version = null;
			
			// Extract version from package name like "org.bukkit.craftbukkit.v1_21_R1"
			// On Paper with Mojang mappings, the structure might be different
			if (serverPackage.contains("craftbukkit")) {
				// Find the part after "craftbukkit."
				int craftbukkitIndex = serverPackage.indexOf("craftbukkit.");
				if (craftbukkitIndex >= 0) {
					String afterCraftbukkit = serverPackage.substring(craftbukkitIndex + "craftbukkit.".length());
					// Get the version part (everything before the next dot, or everything if no dot)
					int nextDot = afterCraftbukkit.indexOf(".");
					if (nextDot > 0) {
						version = afterCraftbukkit.substring(0, nextDot);
					} else if (afterCraftbukkit.length() > 0 && !afterCraftbukkit.equals("craftbukkit")) {
						// Only use if it's not just "craftbukkit"
						version = afterCraftbukkit;
					}
				}
			}
			
			// If version extraction from package failed, try class name
			if (version == null || version.isEmpty() || version.equals("craftbukkit")) {
				Class<?> craftServerClass = Bukkit.getServer().getClass();
				String className = craftServerClass.getName();
				// Extract version from class name like "org.bukkit.craftbukkit.v1_21_R1.CraftServer"
				if (className.contains("craftbukkit")) {
					int craftbukkitIndex = className.indexOf("craftbukkit.");
					if (craftbukkitIndex >= 0) {
						String afterCraftbukkit = className.substring(craftbukkitIndex + "craftbukkit.".length());
						int nextDot = afterCraftbukkit.indexOf(".");
						if (nextDot > 0) {
							version = afterCraftbukkit.substring(0, nextDot);
						} else if (afterCraftbukkit.length() > 0 && !afterCraftbukkit.equals("craftbukkit")) {
							version = afterCraftbukkit;
						}
					}
				}
			}
			
			// Last resort: try to extract from any package segment that looks like a version
			if (version == null || version.isEmpty() || version.equals("craftbukkit")) {
				String[] parts = serverPackage.split("\\.");
				for (String part : parts) {
					// Look for version pattern like v1_21_R1 or 1_21_R1
					if (part.matches("v?\\d+_\\d+_R\\d+") || part.matches("v?\\d+_\\d+_\\d+")) {
						version = part.startsWith("v") ? part.substring(1) : part;
						break;
					}
				}
			}
			
			// If still no version, try to get it from CraftItemStack class directly
			if (version == null || version.isEmpty() || version.equals("craftbukkit")) {
				try {
					// Try to find CraftItemStack class and extract version from its package
					Class<?> craftItemStackClass = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
					String craftItemStackPackage = craftItemStackClass.getPackage().getName();
					if (craftItemStackPackage.contains("craftbukkit")) {
						int craftbukkitIndex = craftItemStackPackage.indexOf("craftbukkit.");
						if (craftbukkitIndex >= 0) {
							String afterCraftbukkit = craftItemStackPackage.substring(craftbukkitIndex + "craftbukkit.".length());
							int nextDot = afterCraftbukkit.indexOf(".");
							if (nextDot > 0) {
								version = afterCraftbukkit.substring(0, nextDot);
							}
						}
					}
				} catch (ClassNotFoundException e) {
					// CraftItemStack not found at expected location - this is OK, we'll try without version
				}
			}
			
			// Clean up version - if it's "craftbukkit" or invalid, set to null (will try without version)
			if (version != null && (version.equals("craftbukkit") || version.isEmpty())) {
				version = null;
			}
			
			// Try to load classes - on Paper with Mojang mappings, classes may not have version in package
			Class<?> loadedCraftItemStackClass = null;
			Class<?> loadedNbtTagCompound = null;
			
			// First, try without version (Paper Mojang mappings style - most common on modern Paper)
			try {
				loadedCraftItemStackClass = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
				// On Paper with Mojang mappings, NBT classes are in net.minecraft.nbt
				loadedNbtTagCompound = Class.forName("net.minecraft.nbt.CompoundTag");
			} catch (ClassNotFoundException e) {
				// Try with version (Spigot-style) if version was detected
				if (version != null && !version.isEmpty() && !version.equals("craftbukkit")) {
					try {
						loadedCraftItemStackClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
						loadedNbtTagCompound = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
					} catch (ClassNotFoundException e2) {
						// Last attempt: try net.minecraft.server without version
						try {
							loadedNbtTagCompound = Class.forName("net.minecraft.server.NBTTagCompound");
						} catch (ClassNotFoundException e3) {
							throw new RuntimeException("Could not find NBT classes. Tried: org.bukkit.craftbukkit.inventory.CraftItemStack, net.minecraft.nbt.CompoundTag, org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack, net.minecraft.server." + version + ".NBTTagCompound, net.minecraft.server.NBTTagCompound. Package: " + serverPackage, e3);
						}
					}
				} else {
					// No valid version, try net.minecraft.server without version as last resort
					try {
						loadedNbtTagCompound = Class.forName("net.minecraft.server.NBTTagCompound");
					} catch (ClassNotFoundException e3) {
						throw new RuntimeException("Could not find CraftItemStack or NBT classes. Package: " + serverPackage + ", className: " + Bukkit.getServer().getClass().getName(), e);
					}
				}
			}
			
			craftItemStackClass = loadedCraftItemStackClass;
			nbtTagCompound = loadedNbtTagCompound;
			Object nbtTag = nbtTagCompound.getDeclaredConstructor().newInstance();
			
			// Try to find NBTBase - location depends on mappings
			@SuppressWarnings("rawtypes")
			Class nbtBase = null;
			try {
				if (version != null && !version.isEmpty()) {
					nbtBase = Class.forName("net.minecraft.server." + version + ".NBTBase");
				}
			} catch (ClassNotFoundException e) {
				// Try Mojang mappings location
				try {
					nbtBase = Class.forName("net.minecraft.nbt.Tag");
				} catch (ClassNotFoundException e2) {
					// Try without version
					try {
						nbtBase = Class.forName("net.minecraft.server.NBTBase");
					} catch (ClassNotFoundException e3) {
						throw new RuntimeException("Could not find NBTBase class", e3);
					}
				}
			}
			craftItemStackAsNMSCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
			ItemStack bukkitItemStack = new ItemStack(Material.COBBLESTONE, 1);
			Object nmsItemStack = craftItemStackAsNMSCopyMethod.invoke(craftItemStackClass, bukkitItemStack);
			craftItemStackAsCraftMirrorMethod = craftItemStackClass.getMethod("asCraftMirror", nmsItemStack.getClass());
			
			// getName() method doesn't exist in newer Paper versions with Mojang mappings
			// Make it optional - if not found, getName() will return null
			try {
				nmsItemStackGetNameMethod = nmsItemStack.getClass().getMethod("getName");
			} catch (NoSuchMethodException e) {
				// Method doesn't exist in this version - this is OK, getName() will return null
				nmsItemStackGetNameMethod = null;
			}
			
			nmsItemStackGetTagMethod = nmsItemStack.getClass().getMethod("getTag");
			nmsItemStackSetTagMethod = nmsItemStack.getClass().getMethod("setTag", nbtTag.getClass());
			nbtTagCompoundHasKeyMethod = nbtTag.getClass().getMethod("hasKey", String.class);
			nbtTagCompoundCMethod = nbtTag.getClass().getMethod("c");
			nbtTagCompoundSetMethod = nbtTag.getClass().getMethod("set", String.class, nbtBase);
			nbtTagCompoundGetCompoundMethod = nbtTag.getClass().getMethod("getCompound", String.class);
			nbtTagCompoundSetStringMethod = nbtTag.getClass().getMethod("setString", String.class, String.class);
			nbtTagCompoundGetStringMethod = nbtTag.getClass().getMethod("getString", String.class);
			nbtTagCompoundSetIntMethod = nbtTag.getClass().getMethod("setInt", String.class, int.class);
			nbtTagCompoundGetIntMethod = nbtTag.getClass().getMethod("getInt", String.class);
			nbtTagCompoundSetDoubleMethod = nbtTag.getClass().getMethod("setDouble", String.class, double.class);
			nbtTagCompoundGetDoubleMethod = nbtTag.getClass().getMethod("getDouble", String.class);
			nbtTagCompoundSetBooleanMethod = nbtTag.getClass().getMethod("setBoolean", String.class, boolean.class);
			nbtTagCompoundGetBooleanMethod = nbtTag.getClass().getMethod("getBoolean", String.class);
			loadedSuccessfully = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean loadedSuccessfully() {
		return loadedSuccessfully;
	}

	public boolean hasKey(ItemStack itemStack, String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return false;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return false;
		return (Boolean) nbtTagCompoundHasKeyMethod.invoke(nbtTag, key);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<String> getNMSKeys(ItemStack itemStack) {
		try {
			Object nmsItemStack = getNMSItemStack(itemStack);
			if (nmsItemStack == null) return new ArrayList<String>();
			Object nbtTag = getNBTTag(nmsItemStack);
			if (nbtTag == null) return new ArrayList<String>();
			Set<String> tags = (Set<String>) nbtTagCompoundCMethod.invoke(nbtTag);
			ArrayList<String> tagArray = new ArrayList<String>(tags);
			return tagArray;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}

	}
	
	
	public Object getNMSItemStack(ItemStack itemStack) {
		try {
			return craftItemStackAsNMSCopyMethod.invoke(craftItemStackClass, itemStack);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public ItemStack getBukkitItemStack(Object nmsItemStack) {
		try {
			return (ItemStack) craftItemStackAsCraftMirrorMethod.invoke(craftItemStackClass, nmsItemStack);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object getNBTTag(Object nmsItemStack) {
		try {
			return nmsItemStackGetTagMethod.invoke(nmsItemStack);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setNBTTag(Object nmsItemStack, Object nbtTag) {
		try {
			nmsItemStackSetTagMethod.invoke(nmsItemStack, nbtTag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public Object generateNBTTag() {
		try {
			return nbtTagCompound.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
	public Object getNBTTagCompoundCompound(Object nbtTag, String key) {
		try {
			return nbtTagCompoundGetCompoundMethod.invoke(nbtTag, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setNBTTagCompoundCompound(Object nbtTag, String key, Object nestedTag) {
		try {
			nbtTagCompoundSetMethod.invoke(nbtTag, key, nestedTag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getNBTTagCompoundString(Object nbtTag, String key) {
		try {
			return (String) nbtTagCompoundGetStringMethod.invoke(nbtTag, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setNBTTagCompoundString(Object nbtTag, String key, String s) {
		try {
			nbtTagCompoundSetStringMethod.invoke(nbtTag, key, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer getNBTTagCompoundInt(Object nbtTag, String key) {
		try {
			return (Integer) nbtTagCompoundGetIntMethod.invoke(nbtTag, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setNBTTagCompoundInt(Object nbtTag, String key, Integer i) {
		try {
			nbtTagCompoundSetIntMethod.invoke(nbtTag, key, i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Double getNBTTagCompoundDouble(Object nbtTag, String key) {
		try {
			return (Double) nbtTagCompoundGetDoubleMethod.invoke(nbtTag, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setNBTTagCompoundDouble(Object nbtTag, String key, Double d) {
		try {
			nbtTagCompoundSetDoubleMethod.invoke(nbtTag, key, d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean getNBTTagCompoundBoolean(Object nbtTag, String key) {
		try {
			return (Boolean) nbtTagCompoundGetBooleanMethod.invoke(nbtTag, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setNBTTagCompoundBoolean(Object nbtTag, String key, Boolean b) {
		try {
			nbtTagCompoundSetBooleanMethod.invoke(nbtTag, key, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Object getCompound(ItemStack itemStack, String key) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return null;
		return getNBTTagCompoundCompound(nbtTag, key);
	}
	public ItemStack setCompound(ItemStack itemStack, String key, Object c) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) nbtTag = generateNBTTag();
		setNBTTagCompoundCompound(nbtTag, key, c);
		setNBTTag(nmsItemStack, nbtTag);
		return getBukkitItemStack(nmsItemStack);
	}
	
	public String getString(ItemStack itemStack, String key) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return null;
		return getNBTTagCompoundString(nbtTag, key);
	}
	public ItemStack setString(ItemStack itemStack, String key, String s) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) nbtTag = generateNBTTag();
		setNBTTagCompoundString(nbtTag, key, s);
		setNBTTag(nmsItemStack, nbtTag);
		return getBukkitItemStack(nmsItemStack);
	}
	
	public Integer getInt(ItemStack itemStack, String key) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return null;
		return getNBTTagCompoundInt(nbtTag, key);
	}
	public ItemStack setInt(ItemStack itemStack, String key, Integer i) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) nbtTag = generateNBTTag();
		setNBTTagCompoundInt(nbtTag, key, i);
		setNBTTag(nmsItemStack, nbtTag);
		return getBukkitItemStack(nmsItemStack);
	}

	public Double getDouble(ItemStack itemStack, String key) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return null;
		return getNBTTagCompoundDouble(nbtTag, key);
	}
	public ItemStack setDouble(ItemStack itemStack, String key, Double d) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) nbtTag = generateNBTTag();
		setNBTTagCompoundDouble(nbtTag, key, d);
		setNBTTag(nmsItemStack, nbtTag);
		return getBukkitItemStack(nmsItemStack);
	}

	public Boolean getBoolean(ItemStack itemStack, String key) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return null;
		return getNBTTagCompoundBoolean(nbtTag, key);
	}
	public ItemStack setBoolean(ItemStack itemStack, String key, Boolean b) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) nbtTag = generateNBTTag();
		setNBTTagCompoundBoolean(nbtTag, key, b);
		setNBTTag(nmsItemStack, nbtTag);
		return getBukkitItemStack(nmsItemStack);
	}


	
	
	public String getMobEggType(ItemStack itemStack) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) return null;
		Object entityTag = getNBTTagCompoundCompound(nbtTag, "EntityTag");
		if (entityTag == null) return null;
		return getNBTTagCompoundString(entityTag, "id");
	}
	public ItemStack setMobEggType(ItemStack itemStack, String type) {
		Object nmsItemStack = getNMSItemStack(itemStack);
		if (nmsItemStack == null) return null;
		Object nbtTag = getNBTTag(nmsItemStack);
		if (nbtTag == null) nbtTag = generateNBTTag();
		Object entityTag = getNBTTagCompoundCompound(nbtTag, "EntityTag");
		if (entityTag == null) entityTag = generateNBTTag();
		setNBTTagCompoundString(entityTag, "id", type);
		setNBTTagCompoundCompound(nbtTag, "EntityTag", entityTag);
		setNBTTag(nmsItemStack, nbtTag);
		return getBukkitItemStack(nmsItemStack);
	}
	
	
	public String getName(ItemStack itemStack) {
		try {
			if (nmsItemStackGetNameMethod == null) {
				// Method not available in this server version
				return null;
			}
			Object nmsItemStack = getNMSItemStack(itemStack);
			if (nmsItemStack == null) return null;
			return (String) nmsItemStackGetNameMethod.invoke(nmsItemStack);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
