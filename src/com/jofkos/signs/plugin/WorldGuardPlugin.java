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
	public boolean canBuild(Player player, Block block) {
		return getWorldGuard().canBuild(player, block);
	}
	
	public boolean isInOwnedRegion(Player player, Block block) {
		LocalPlayer localPlayer = getWorldGuard().wrapPlayer(player);
		for (ProtectedRegion pr : getWorldGuard().getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
			if (pr.isMember(localPlayer)) {
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