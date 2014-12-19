package com.jofkos.signs.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.jofkos.signs.utils.Config.Action;

@SuppressWarnings({"deprecation"})
public class Utils {

	private static final List<Material> SIGNS = Arrays.asList(Material.WALL_SIGN, Material.SIGN_POST, Material.SIGN);

	public static boolean isSign(Block b) {
		return SIGNS.contains(b.getType());
	}

	public static void write(Block clickedBlock, BlockFace clicked, Player writer, String... texts) {
		if (!isSign(clickedBlock)) return;
		
		BlockFace left;
		switch (clicked) {
		case EAST:
			left = BlockFace.SOUTH; break;
		case NORTH:
			left = BlockFace.EAST; break;
		case SOUTH:
			left = BlockFace.WEST; break;
		case WEST:
			left = BlockFace.NORTH; break;
		default:
			return;
		}
		
		String text = "";
		
		for (String line : texts) {
			line = line.replace("\n", " ");
			if (!text.endsWith(" ") && !line.startsWith(" ")) {
				text += " ";
			}

			text += line;
		}
		
		text = ChatColor.translateAlternateColorCodes('&', text).replace("\t", " ").replace("  ", " ");
		
		Block sign = clickedBlock;
		
		while (isSign(sign.getRelative(left))) {
			sign = sign.getRelative(left);
		}

		while (isSign(sign.getRelative(BlockFace.UP))) {
			sign = sign.getRelative(BlockFace.UP);
		}

		clickedBlock = sign;

		while (isSign(clickedBlock)) {
			while (isSign(sign)) {
				if (API.canBuild(writer, sign)) {
					for (int i = 0; i < 4; i++) {
						Sign s = (Sign) sign.getState();
						if (text.length() <= 15) {
							s.setLine(i, text);
							s.update();
							return;
						}
						if (text.startsWith(" ")) {
							text = text.substring(1);
						}
						s.setLine(i, text.substring(0, 15));
						text = text.substring(15);
						s.update();
					}
				}
				sign = sign.getRelative(left.getOppositeFace());
			}
			sign = clickedBlock = clickedBlock.getRelative(BlockFace.DOWN);
		}
	}

	public static boolean isAction(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		switch (Config.ACTION) {
		case HAND:
			return !p.isSneaking();
		case ITEM:
			return !p.isSneaking() && Config.EDIT_MAT.equals(e.getItem());
		case SNEAK:
			return p.isSneaking();
		}
		return false;
	}
	
	public static void cost(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (Config.COSTS && Config.ACTION == Action.ITEM && p.getGameMode() != GameMode.CREATIVE && !p.hasPermission("signs.bypass.editcost") && !p.hasPermission("signs.bypass.*")) {
			if (p.getItemInHand().getAmount() <= 1) {
				p.getInventory().clear(p.getInventory().getHeldItemSlot());
			} else {
				p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
			}
		}
	}
	
//	---------------------------- Item ----------------------------	\\
	
	public static void addGlow(ItemStack i) {
		if (i.containsEnchantment(GlowEnchantment.getGlow())) return;
		i.addEnchantment(GlowEnchantment.getGlow(), 1);
	}

	public static void giveItem(Player p, ItemStack i) {
		if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
			p.setItemInHand(i);
		} else {
			Iterator<ItemStack> ite = p.getInventory().addItem(i).values().iterator();
			while (ite.hasNext()) {
				p.getWorld().dropItem(p.getLocation(), ite.next());
			}
		}
		p.updateInventory();
	}
	
//	---------------------------- Text ----------------------------	\\
	
	public static String[] colorCodes(String... lines) {
		String[] r = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			r[i] = lines[i].replaceAll(ChatColor.COLOR_CHAR + "([a-z0-9])", "&$1");
			while (r[i].startsWith("&0") || r[i].startsWith("§0")) {
				r[i] = r[i].substring(2);
			}
		}
		return r;
	}
	
	public static void clearLines(SignChangeEvent e) {
		for (int i = 0; i < e.getLines().length; i++) {
			e.setLine(i, clearLine(e.getLine(i)));
		}
	}
	
	public static String clearLine(String line) {
		StringBuilder builder = new StringBuilder();
		for (char c : line.toCharArray()) {
			if (c < 0xF700 || c > 0xF747) {
				builder.append(c);
			}
		}
		return builder.toString();
	}
}

