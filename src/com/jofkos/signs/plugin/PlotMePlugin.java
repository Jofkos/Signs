package com.jofkos.signs.plugin;

import com.jofkos.signs.utils.API;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlotMePlugin extends API.APIPlugin {
	
	static {
		clazz = "com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin";
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
		BukkitBlock bukkitBlock = new BukkitBlock(block);
		if (!manager.isPlotWorld(bukkitBlock) || player.hasPermission("plotme.admin.buildanywhere")) {
			return true;
		}
		
		String id = manager.getPlotId(bukkitBlock.getLocation());
		
		if (id == null || id.equalsIgnoreCase("")) {
			return false;
		}

		Plot plot = manager.getMap(new BukkitPlayer(player)).getPlot(id);
		return plot != null && plot.isAllowed(player.getUniqueId());
	}
}