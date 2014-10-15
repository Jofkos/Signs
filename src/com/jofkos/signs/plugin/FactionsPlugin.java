package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.API;
import com.massivecraft.factions.listeners.FactionsListenerMain;
import com.massivecraft.massivecore.ps.PS;

public class FactionsPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.massivecraft.factions.Factions";
	}

	@Override
	public boolean canBuild(Player p, Block b) {
		return FactionsListenerMain.canPlayerBuildAt(p, PS.valueOf(b), true);
	}

}
