package com.jofkos.signs.plugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.griefcraft.lwc.LWC;
import com.jofkos.signs.utils.API;

public class LWCPlugin extends API.APIPlugin {
	
	static {
		clazz = "com.griefcraft.lwc.LWCPlugin";
	}
	
	@Override
	public boolean canBuild(Player p, Block b) {
		return LWC.getInstance().findProtection(b) == null ? true : LWC.getInstance().canAccessProtection(p, b);
	}
}