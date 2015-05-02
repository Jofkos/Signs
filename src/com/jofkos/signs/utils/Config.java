package com.jofkos.signs.utils;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.jofkos.signs.Signs;
import com.jofkos.signs.utils.i18n.I18n;

public class Config {
	
	private static Signs plugin;
	private static FileConfiguration config;
	
	public static MaterialType EDIT_MAT = new MaterialType(Material.INK_SACK);
	
	public static Locale LOCALE = Locale.getDefault();
	public static Action ACTION = null;
	public static boolean COSTS = false;
	public static boolean COLORS = true;
	public static boolean PER_COLOR_PERMISSIONS = false;
	public static boolean ONLY_OWNED = false;
	public static boolean NOTIFY_UPDATES = true;
	public static boolean AUTO_UPDATE = true;
	
	public static MaterialType COPY_MAT = new MaterialType(Material.PAPER);
	public static MaterialType INK = new MaterialType(Material.INK_SACK);
	public static boolean COPY_COSTS = false;
	public static boolean PASTE_COSTS = false;
	
	public static void load() {
		plugin = Signs.getInstance();
		config = plugin.getConfig();
		setupConfig();
		loadConfig();
	}
	
	public static void reload() {
		loadConfig();
	}
	
	private static void setupConfig() {
		
		if (config.contains("EditMaterial")) {
			config.set("Action", config.get("EditMaterial"));
			config.set("EditMaterial", null);
			saveConfig();
		}
		
		if (config.contains("EditCost")) {
			config.set("EditCosts", config.get("EditCost"));
			config.set("EditCost", null);
			saveConfig();
		}
		
		config.addDefault("Locale", LOCALE.toString());
		config.addDefault("Action", "INK_SACK:0");
		config.addDefault("EditCosts", false);
		config.addDefault("SignColors", true);
		config.addDefault("PerColorPermissions", false);
		config.addDefault("OnlyInOwnedRegion", false);
		config.addDefault("NotifyUpdates", true);
		config.addDefault("AutoUpdate", true);
		
		config.addDefault("Copy.Item", "PAPER");
		config.addDefault("Copy.Ink", "INK_SACK:0");
		config.addDefault("Copy.CopyCosts", false);
		config.addDefault("Copy.PasteCosts", false);
		
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	private static void loadConfig() {
		reloadConfig();
		
		LOCALE = LocaleUtils.toLocale(config.getString("Locale"));
		ACTION = Action.fromString(config.getString("Action"));
		COSTS = config.getBoolean("EditCosts");
		COLORS = config.getBoolean("SignColors");
		PER_COLOR_PERMISSIONS = config.getBoolean("PerColorPermissions");
		ONLY_OWNED = config.getBoolean("OnlyInOwnedRegion");
		NOTIFY_UPDATES = config.getBoolean("NotifyUpdates");
		AUTO_UPDATE = config.getBoolean("AutoUpdate");
		
		COPY_MAT = MaterialType.fromString(config.getString("Copy.Item"));
		INK = MaterialType.fromString(config.getString("Copy.Ink"));
		COPY_COSTS = config.getBoolean("Copy.CopyCosts");
		PASTE_COSTS = config.getBoolean("Copy.PasteCosts");
		
		if (ACTION == null) {
			reset("Action", ACTION = Action.fromString("INK_SACK:0"), I18n._("config.edit.invalid"), I18n._("config.edit.resetted", "(INK_SACK:0)"));
		}

		if (COPY_MAT == null) {
			reset("Copy.Item", COPY_MAT = MaterialType.fromString("PAPER"), I18n._("config.copy.invalid"), I18n._("config.copy.resetted", "(PAPER)"));
		}

		if (INK == null) {
			reset("Copy.Ink", INK = MaterialType.fromString("INK_SACK:0"), I18n._("config.ink.invalid"), I18n._("config.ink.resetted", "(INK_SACK:0)"));
		}
	}
	
	private static void reset(String path, Object obj, String... messages) {
		Signs.log(messages);
		config.set(path, obj);
		saveConfig();
	}
	
	private static void saveConfig() {
		plugin.saveConfig();
	}
	
	public static void reloadConfig() {
		plugin.reloadConfig();
		config = plugin.getConfig();
	}
	
	public enum Action {
		HAND, ITEM, SNEAK;
		
		public static Action fromString(String action) {
			action = action.trim();
			if (action.equalsIgnoreCase("hand")) {
				return Action.HAND;
			} else if (action.equalsIgnoreCase("sneak")) {
				return Action.SNEAK;
			} else {
				return (EDIT_MAT = MaterialType.fromString(action)) != null ? Action.ITEM : null;
			}
		}
		
		public String toString() {
			return this == ITEM ? EDIT_MAT.toString() : name();
		}
	}
}