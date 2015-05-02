package com.jofkos.signs.plugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.API;
import com.jofkos.signs.utils.Utils;

import fr.neatmonster.nocheatplus.checks.blockplace.BlockPlaceData;
import fr.neatmonster.nocheatplus.checks.blockplace.BlockPlaceListener;

public class NoCheatPlusPlugin extends API.APIPlugin {

	static {
		clazz = "fr.neatmonster.nocheatplus.NoCheatPlus";
	}

	@Override
	public boolean canBuild(Player player, Block block) {
		
		if (Utils.isSign(block)) {
			BlockPlaceData blockData = BlockPlaceData.getData(player);
			blockData.autoSignPlacedTime = System.currentTimeMillis();
			blockData.autoSignPlacedHash = BlockPlaceListener.getBlockPlaceHash(block, Material.SIGN);
		}
		
		return true;
	}
}
