package com.jofkos.signs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.jofkos.signs.listeners.Listeners;
import com.jofkos.signs.utils.API;
import com.jofkos.signs.utils.Config;
import com.jofkos.signs.utils.GlowEnchantment;
import com.jofkos.signs.utils.Updater;
import com.jofkos.signs.utils.i18n.I18n;

public class Signs extends JavaPlugin {
	private static Signs plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		Config.load();
		
		I18n.load();
		API.load();
		ReloadCommand.load();
		Listeners.load();
		GlowEnchantment.load();
		
		Updater.checkVersion();
		
		this.saveConfig();
	}
	
	@Override
	public void onDisable() {
		try {
			Updater.moveTemp();
		} catch (Exception e) {
			System.err.println("An error occured while trying to install the update. Please report that if it occurs again.");
		}
	}
	
	public static Signs getInstance() {
		return plugin;
	}
	
	public static void log(String... msgs) {
		for (String msg : msgs) {
			log(msg);
		}
	}
	
	public static void log(String msg) {
		Bukkit.getConsoleSender().sendMessage("[Signs] " + msg);
	}
}
