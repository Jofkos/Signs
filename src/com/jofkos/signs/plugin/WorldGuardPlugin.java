package com.jofkos.signs.plugin;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jofkos.signs.utils.API;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.sk89q.worldguard.bukkit.WorldGuardPlugin";
	}
	
	@Override
	public boolean canBuild(Player p, Block b) {
		return getWorldGuard().getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation()).canBuild(getWorldGuard().wrapPlayer(p));
	}
	
	public boolean isInOwnedRegion(Player p, Block b) {
		for (ProtectedRegion pr : getWorldGuard().getRegionManager(b.getWorld()).getApplicableRegions(b.getLocation())) {
			if (pr.getOwners().contains(p.getName())) {
				return true;
			}
			if (pr.getMembers().contains(p.getName())) {
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