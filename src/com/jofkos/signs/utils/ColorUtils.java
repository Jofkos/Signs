package com.jofkos.signs.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.jofkos.signs.Signs;

public class ColorUtils implements Listener {
	
	private static Map<UUID, String> patterns;
	
	static {
		Signs.getInstance().getServer().getPluginManager().registerEvents(new ColorUtils(), Signs.getInstance());
	}
	
	public static void load() {
		if (patterns != null)
			patterns.clear();
		if (Config.PER_COLOR_PERMISSIONS && patterns == null) 
			patterns = new HashMap<UUID, String>();
	}
	
	public static String translateAlternateColorCodes(Player player, String text) {
		boolean perColorPerms = Config.PER_COLOR_PERMISSIONS && player != null;
		
		if (perColorPerms && !patterns.containsKey(player.getUniqueId())) {
			String pattern = "&(?=[";
			for (ChatColor color : ChatColor.values()) {
				if (player.hasPermission("signs.signcolors." + color.name().toLowerCase())) {
					pattern += color.getChar();
				}
			}
			patterns.put(player.getUniqueId(), pattern + "])");
			pattern = null;
		}
		
		return perColorPerms ? text.replaceAll(patterns.get(player.getUniqueId()), String.valueOf(ChatColor.COLOR_CHAR)) : ChatColor.translateAlternateColorCodes('&', text);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (patterns != null)
			patterns.remove(e.getPlayer().getUniqueId());
	}
	
}