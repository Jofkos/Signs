package com.jofkos.signs.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.jofkos.signs.Signs;

public class Listeners {
	
	public static void load() {
		for (Listener l : Arrays.asList(
				new EditListener(),
				new CopyListener(),
				new ColorListener())
		) Bukkit.getPluginManager().registerEvents(l, Signs.getInstance());
	}
	
}
