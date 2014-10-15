package com.jofkos.signs.utils;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NMSUtils {

	private static String NMSVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private static Class<?> packet = getClass("nms.Packet");
	private static Class<?> tileEntitySign = getClass("nms.TileEntitySign");
	private static Class<?> entityHuman = getClass("nms.EntityHuman");

	private static Class<?> craftPlayer = getClass("obc.entity.CraftPlayer");
	private static Class<?> craftWorld = getClass("obc.CraftWorld");
	private static Class<?> craftServer = getClass("obc.CraftServer");

	private static Class<?> getClass(String name) {
		try {
			name = name.replaceAll("(obc|nms)", "$1\\." + NMSVersion);
			name = name.replace("obc", "org.bukkit.craftbukkit");
			name = name.replace("nms", "net.minecraft.server");
			return Class.forName(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void sendPacket(Player p, Object o) {
		try {
			if (!packet.isAssignableFrom(o.getClass())) {
				throw new IllegalArgumentException(o.getClass().getCanonicalName() + " is not a " + packet.getCanonicalName());
			}

			Reflect.invoke(Reflect.get(Reflect.invoke(craftPlayer.cast(p), "getHandle"), "playerConnection"), "sendPacket", o, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getPacket(String name) {
		try {
			Class<?> p = getClass("nms." + name);

			if (!packet.isAssignableFrom(p)) {
				throw new IllegalArgumentException(p.getCanonicalName() + " is not a " + packet.getCanonicalName());
			}
			return p.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object getSignEditPacket(int x, int y, int z) {
		try {
			Object p = getPacket("PacketPlayOutOpenSignEditor");
			Reflect.set(p, "a", x);
			Reflect.set(p, "b", y);
			Reflect.set(p, "c", z);

			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getSignChange(Block sign, String... lines) {
		return getSignChange(sign.getX(), sign.getY(), sign.getZ(), lines);
	}

	public static Object getSignChange(int x, int y, int z, String... lines) {
		lines = Utils.colorCodes(lines);
		try {
			Object p = getPacket("PacketPlayOutUpdateSign");
			Reflect.set(p, "x", x);
			Reflect.set(p, "y", y);
			Reflect.set(p, "z", z);
			Reflect.set(p, "lines", new String[] { lines[0], lines[1], lines[2], lines[3] });

			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void sendSignEditor(Player p, Block sign) {
		try {
			Object tile = getTile(sign);
			Reflect.set(tile, "isEditable", true);
			Reflect.invoke(tile, "a", Reflect.invoke(craftPlayer.cast(p), "getHandle"), entityHuman);
			sendPacket(p, getSignEditPacket(sign.getX(), sign.getY(), sign.getZ()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getTile(Block sign) {
		try {
			return tileEntitySign.cast(Reflect.invoke(Reflect.invoke(craftWorld.cast(sign.getWorld()), "getHandle"), "getTileEntity", sign.getX(), sign.getY(), sign.getZ()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getVersion() {
		try {
			return Reflect.invoke(Reflect.invoke(craftServer.cast(Bukkit.getServer()), "getServer"), "getVersion");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
