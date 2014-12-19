package com.jofkos.signs.utils.nms;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.jofkos.signs.utils.PacketReflecter;
import com.jofkos.signs.utils.Reflect;
import com.jofkos.signs.utils.Reflecter;
import com.jofkos.signs.utils.Utils;

public class NMSImpl17 implements NMSCore {

	@Override
	public Object getSignEdit(int x, int y, int z) {
		try {
			Reflecter packet = new PacketReflecter("PacketPlayOutOpenSignEditor");
			packet.set("a", x);
			packet.set("b", y);
			packet.set("c", z);
			
			return packet.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getSignChange(Block sign, String... lines) {
		lines = Utils.colorCodes(lines);
		try {
			Reflecter packet = new PacketReflecter("PacketPlayOutUpdateSign");
			
			packet.set("x", sign.getX());
			packet.set("y", sign.getY());
			packet.set("z", sign.getZ());
			packet.set("lines", new String[] { lines[0], lines[1], lines[2], lines[3] });
			
			return packet.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void sendSignEditor(Player p, Block sign) {
		try {
			Object tile = getTileEntity(sign);
			Reflect.set(tile, "isEditable", true);
			Reflect.invoke(tile, "a", Reflect.invoke(craftPlayer.cast(p), "getHandle"), entityHuman);
			NMSUtils.sendPacket(p, getSignEdit(sign.getX(), sign.getY(), sign.getZ()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getTileEntity(Block sign) {
		try {
			return tileEntitySign.cast(Reflect.invoke(Reflect.invoke(craftWorld.cast(sign.getWorld()), "getHandle"), "getTileEntity", sign.getX(), sign.getY(), sign.getZ()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
