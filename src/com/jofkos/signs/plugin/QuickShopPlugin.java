package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.maxgamer.QuickShop.QuickShop;
import org.maxgamer.QuickShop.Shop.Shop;
import org.maxgamer.QuickShop.Util.Util;

import com.jofkos.signs.utils.API;

public class QuickShopPlugin extends API.APIPlugin {
	
	static {
		clazz = "org.maxgamer.QuickShop.QuickShop";
	}
	
	@Override
	public boolean canBuild(Player p, Block b) {
		return getShop(b) == null;
	}
	
	private Shop getShop(Block b) {
		return QuickShop.instance.getShopManager().getShop(Util.getAttached(b).getLocation());
	}
	
}