package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.Acrobot.ChestShop.Security;
import com.jofkos.signs.utils.API;

public class ChestShopPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.Acrobot.ChestShop.ChestShop";
	}
	
	@Override
	public boolean canBuild(Player player, Block block) {
		return Security.canAccess(player, block);
	}

}
