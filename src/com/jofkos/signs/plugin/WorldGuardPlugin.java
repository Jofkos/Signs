package com.jofkos.signs.plugin;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jofkos.signs.utils.API;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.sk89q.worldguard.bukkit.WorldGuardPlugin";
	}
		
	@Override
	public boolean canBuild(Player p, Block b) {
		return getWorldGuard().canBuild(p, b);
	}
	
	public boolean isInOwnedRegion(Player p, Block b) {
		LocalPlayer player = getWorldGuard().wrapPlayer(p);
		for (ProtectedRegion pr : getWorldGuard().getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation())) {
			if (pr.isMember(player)) {
				return true;
			}
		}
		return false;
	}
	
	private com.sk89q.worldguard.bukkit.WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if ((plugin == null) || (!(plugin instanceof com.sk89q.worldguard.bukkit.WorldGuardPlugin))) {
			return null;
		}
		
		return (com.sk89q.worldguard.bukkit.WorldGuardPlugin) plugin;
	}
}