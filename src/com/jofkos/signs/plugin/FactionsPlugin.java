package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.API;
import com.massivecraft.factions.engine.EngineMain;
import com.massivecraft.factions.listeners.FactionsListenerMain;
import com.massivecraft.massivecore.ps.PS;

public class FactionsPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.massivecraft.factions.Factions";
	}
	
	private boolean oldAPI = false;
	
	public FactionsPlugin() {
		try {
			EngineMain.class.getName();
		} catch (NoClassDefFoundError e) {
			oldAPI = true;
		}
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		if (oldAPI) {
			return FactionsListenerMain.canPlayerBuildAt(player, PS.valueOf(block), true);			
		} else {
			return EngineMain.canPlayerBuildAt(player, PS.valueOf(block), true);
		}
	}

}
