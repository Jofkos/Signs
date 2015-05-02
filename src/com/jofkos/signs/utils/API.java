package com.jofkos.signs.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jofkos.signs.Signs;
import com.jofkos.signs.plugin.WorldGuardPlugin;
import com.jofkos.signs.utils.i18n.I18n;

public class API {
	
	private static Map<String, APIPlugin> plugins = new HashMap<String, APIPlugin>();
	
	public static void load() {
		for (String dependency : Signs.getInstance().getDescription().getSoftDepend()) {
			try {
				Class<?> apiPlugin = Class.forName("com.jofkos.signs.plugin." + dependency + "Plugin");
				Class<?> pluginClass = Class.forName((String) apiPlugin.getField("clazz").get(null));
				
				Plugin plugin = Bukkit.getPluginManager().getPlugin(dependency);
				
				if (pluginClass != null && plugin != null && pluginClass.isInstance(plugin) && plugin.isEnabled()) {
					plugins.put(dependency.toLowerCase(), (APIPlugin) apiPlugin.newInstance());
				}
			} catch (Exception e) {}
		}
		if (plugins.size() > 0) {
			Signs.log(I18n._("modules.loaded", plugins.keySet().toString().replaceAll("(^\\[)|(\\]$)", "").replaceAll("\\,(?!.*\\,)", " " + I18n._("words.and"))));
		}
	}
	
	public static boolean canBuild(Player player, Block block) {
		if (player == null) return true;
		if (!player.hasPermission("signs.use") && !player.isOp()) return false;
		if (player.isOp() || player.hasPermission("signs.bypass.*")) return true;
		
		Iterator<String> iterator = plugins.keySet().iterator();
		
		while (iterator.hasNext()) {
			String plugin = iterator.next();
			
			try {
				if (!canBuild(plugin, player, block)) {
					return false;
				}
			} catch (Exception | Error e) {
				e.printStackTrace();
				
				iterator.remove();
				
				Signs.log("The " + plugin + " integration doesn't works properly, it has been removed from this session.");
				Signs.log("Please report that error and/or update the plugin (and its dependencies)");
			}
		}
		return true;
	}
	
	public static boolean canBuild(String plugin, Player player, Block block) {
		plugin = plugin.toLowerCase();
		if (plugins.keySet().contains(plugin)) {
			return player.hasPermission("signs.bypass." + plugin) || plugins.get(plugin).canBuild(player, block);
		}
		return true;
	}
	
	public static boolean isInOwnedRegion(Player player, Block block) {
		return Config.ONLY_OWNED && plugins.keySet().contains("worldguard") && !player.hasPermission("signs.bypass.worldguard") && !player.isOp() ? ((WorldGuardPlugin) plugins.get("worldguard")).isInOwnedRegion(player, block) : true;
	}
	
	public static abstract class APIPlugin {
		public static String clazz;
		public abstract boolean canBuild(Player player, Block block);
	}
}
