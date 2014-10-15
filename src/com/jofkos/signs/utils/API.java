package com.jofkos.signs.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jofkos.signs.Signs;
import com.jofkos.signs.plugin.WorldGuardPlugin;
import com.jofkos.signs.utils.i18n.I18n;

public class API {
	
	private static HashMap<String, APIPlugin> plugins = new HashMap<String, APIPlugin>();
	
	public static void load() {
		for (String s : Signs.getInstance().getDescription().getSoftDepend()) {
			try {
				Class<?> p = Class.forName("com.jofkos.signs.plugin." + s + "Plugin");
				Class<?> c = Class.forName((String) p.getField("clazz").get(null));
				
				Plugin a = Bukkit.getPluginManager().getPlugin(s);
				
				if (c != null && a != null && c.isInstance(a) && a.isEnabled()) {
					plugins.put(s.toLowerCase(), (APIPlugin) p.newInstance());
				}
			} catch (Exception e) {}
		}
		if (plugins.size() > 0) {
			Signs.log(I18n._("modules.loaded", plugins.keySet().toString().replaceAll("(^\\[)|(\\]$)", "").replaceAll("\\,(?!.*\\,)", " " + I18n._("words.and"))));
		}
	}
	
	public static boolean canBuild(Player p, Block b) {
		if (p == null) return true;
		if (!p.hasPermission("signs.use") && !p.isOp()) return false;
		if (p.isOp() || p.hasPermission("signs.bypass.*")) return true;
		
		for (String a : plugins.keySet()) {
			if (!canBuild(a, p, b)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean canBuild(String plugin, Player p, Block b) {
		plugin = plugin.toLowerCase();
		if (plugins.keySet().contains(plugin)) {
			return p.hasPermission("signs.bypass." + plugin) || plugins.get(plugin).canBuild(p, b);
		}
		return true;
	}
	
	public static boolean isInOwnedRegion(Player p, Block b) {
		return Config.ONLY_OWNED && plugins.keySet().contains("worldguard") && !p.hasPermission("signs.bypass.worldguard") && !p.isOp() ? ((WorldGuardPlugin) plugins.get("worldguard")).isInOwnedRegion(p, b) : true;
	}
	
	public static abstract class APIPlugin {
		public static String clazz;
		public abstract boolean canBuild(Player p, Block b);
	}
}
