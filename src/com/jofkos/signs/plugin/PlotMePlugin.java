package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.API;
import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;
import com.worldcretornica.plotme.PlotMe;

public class PlotMePlugin extends API.APIPlugin {
	
	static {
		clazz = "com.worldcretornica.plotme.PlotMe";
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		if (!PlotManager.isPlotWorld(block) || PlotMe.cPerms(player, "plotme.admin.buildanywhere")) {
			return true;
		}
		
		String id = PlotManager.getPlotId(block.getLocation());
		
		if (id.equalsIgnoreCase("") || id == null) {
			return false;
		}
		
		Plot plot = (Plot) PlotManager.getMap(player).plots.get(id);
		return plot != null && plot.isAllowed(player.getUniqueId());
	}
}