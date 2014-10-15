package com.jofkos.signs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jofkos.signs.utils.Config;
import com.jofkos.signs.utils.i18n.I18n;

public class ReloadCommand implements CommandExecutor {
	
	public static void load() {
		Signs.getInstance().getCommand("signs").setExecutor(new ReloadCommand());
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		
		Config.reload();
		I18n.load();
		Command.broadcastCommandMessage(cs, I18n._("config.reloaded"));
		
		return true;
	}

}
