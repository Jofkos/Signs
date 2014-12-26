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
	
	private boolean oldAPI;
	
	public FactionsPlugin() {		
		try {
			EngineMain.class.getName();
			oldAPI = false;
		} catch (NoClassDefFoundError e) {
			oldAPI = true;
		}
	}
	
	@Override
	public boolean canBuild(Player p, Block b) {
		if (oldAPI) {
			return FactionsListenerMain.canPlayerBuildAt(p, PS.valueOf(b), true);			
		} else {
			return EngineMain.canPlayerBuildAt(p, PS.valueOf(b), true);
		}
	}

}
