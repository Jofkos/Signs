package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.yi.acru.bukkit.Lockette.Lockette;

import com.jofkos.signs.utils.API;

public class LockettePlugin extends API.APIPlugin {
	
	static {
		clazz = "org.yi.acru.bukkit.Lockette.Lockette";
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		block = Lockette.getSignAttachedBlock(block);
		return block != null && Lockette.isProtected(block) ? Lockette.isOwner(block, player) : true;
	}

}