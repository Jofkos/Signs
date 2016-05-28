/*
 * Copyright (c) 2016 Jofkos. All rights reserved.
 */

package com.jofkos.signs.utils.nms;

import com.jofkos.signs.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class NMSImpl19 extends NMSImpl18 {

	@Override
	public Object getSignChange(Block sign, String... lines) {
		try {
			Sign signState = (Sign) sign.getState();

			String[] colorCodes = Utils.colorCodes(lines);
			for (int i = 0; i < 4; i++) {
				signState.setLine(i, colorCodes[i]);

			}
			signState.update();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
