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
	public boolean canBuild(Player p, Block b) {
		if (!PlotManager.isPlotWorld(b) || PlotMe.cPerms(p, "plotme.admin.buildanywhere")) {
			return true;
		}
		
		String id = PlotManager.getPlotId(b.getLocation());
		
		if (id.equalsIgnoreCase("") || id == null) {
			return false;
		}
		
		Plot plot = (Plot) PlotManager.getMap(p).plots.get(id);
		return plot != null && plot.isAllowed(p.getUniqueId());
	}
}