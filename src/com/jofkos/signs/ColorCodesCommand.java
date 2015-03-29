package com.jofkos.signs;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jofkos.signs.utils.Config;

public class ColorCodesCommand implements CommandExecutor {
	
	public static void load() {
		Signs.getInstance().getCommand("colorcodes").setExecutor(new ColorCodesCommand());
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		
		for (ChatColor color : ChatColor.values()) {
			if (Config.PER_COLOR_PERMISSIONS && !cs.hasPermission("signs.signcolors." + color.name().toString())) continue;
			if (color != ChatColor.MAGIC) {
				cs.sendMessage(color + "&" + color.getChar() + " " + WordUtils.capitalizeFully(color.name()).replace("_", " "));
			} else {
				cs.sendMessage("&" + color.getChar() + " " + WordUtils.capitalizeFully(color.name()).replace("_", " ") + " " + color + WordUtils.capitalizeFully(color.name()).replace("_", " "));
			}
		}
		
		return true;
	}

}
