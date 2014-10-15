package com.jofkos.signs.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.jofkos.signs.utils.Config;
import com.jofkos.signs.utils.Utils;

public class ColorListener implements Listener {
	
	@EventHandler
	public void onSignChangeColor(SignChangeEvent e) {
		Utils.clearLines(e);
		if (!Config.COLORS) return; 
		if (e.getPlayer().hasPermission("signs.signcolors")) {
			for (int i = 0; i < 4; i++) {
				if (!e.getLine(i).isEmpty()) {
					String updated = ChatColor.translateAlternateColorCodes('&', e.getLine(i));
					e.setLine(i, updated);
				}
			}
		}
	}
	
}
