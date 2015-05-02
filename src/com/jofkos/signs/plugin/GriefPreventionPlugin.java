package com.jofkos.signs.plugin;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.API;

public class GriefPreventionPlugin extends API.APIPlugin {
	
	static {
		clazz = "me.ryanhamshire.GriefPrevention.GriefPrevention";
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		return GriefPrevention.instance.allowBuild(player, block.getLocation()) == null;
	}

}
