package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.API;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

@SuppressWarnings({"deprecation"})
public class TownyPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.palmergames.bukkit.towny.Towny";
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		try {
			return PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getTypeId(), block.getData(), TownyPermission.ActionType.BUILD);
		} catch (Exception e) {
			return true;
		}
	}

}