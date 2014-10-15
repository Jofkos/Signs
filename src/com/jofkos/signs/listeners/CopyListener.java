package com.jofkos.signs.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jofkos.signs.utils.API;
import com.jofkos.signs.utils.Config;
import com.jofkos.signs.utils.Utils;
import com.jofkos.signs.utils.i18n.I18n;

@SuppressWarnings({ "deprecation" })
public class CopyListener implements Listener {

	@EventHandler
	public void onCopy(PlayerInteractEvent e) {
		if (!e.getAction().toString().contains("CLICK_BLOCK"))
			return;
		if (e.getPlayer().isSneaking())
			return;
		if (!Utils.isSign(e.getClickedBlock()))
			return;
		if (!Config.COPY_MAT.equals(e.getItem()))
			return;
		if (!API.canBuild(e.getPlayer(), e.getClickedBlock()))
			return;
		if (!e.getPlayer().hasPermission("signs.copy"))
			return;
		e.setCancelled(true);

		Player p = e.getPlayer();
		Sign sign = (Sign) e.getClickedBlock().getState();
		ItemStack item = e.getItem();
		ItemMeta meta = item.getItemMeta();
		String itemName = I18n._("writtenItem.name", WordUtils.capitalize(item.getType().toString().replace("_", " ").toLowerCase()));

		switch (e.getAction()) {
		case RIGHT_CLICK_BLOCK:
			if (!p.getInventory().contains(Config.INK.getMaterial(), 1) && Config.COPY_COSTS && !canBypass(p, "copycosts"))
				return;

			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			} else {
				p.setItemInHand(null);
			}

			ItemStack single = item.clone();
			single.setAmount(1);
			meta.setDisplayName(itemName);

			List<String> oldLore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();

			List<String> lore = new ArrayList<String>();
			for (String s : sign.getLines()) {
				lore.add("§r§d>§r§7§o " + s.replaceAll("§([0-9a-fk-or])", "&$1"));
			}
			meta.setLore(lore);
			single.setItemMeta(meta);

			if (!meta.hasEnchants())
				Utils.addGlow(single);
			Utils.giveItem(p, single);

			if (!oldLore.equals(lore) && Config.COPY_COSTS && !canBypass(p, "copycosts")) {
				p.getInventory().removeItem(Config.INK.toItemStack());
			}
			break;
		case LEFT_CLICK_BLOCK:
			if (!meta.getDisplayName().equals(itemName))
				return;

			if (!p.getInventory().contains(Config.INK.getMaterial(), 1) && Config.PASTE_COSTS && !canBypass(p, "pastecosts"))
				return;

			String[] originLines = sign.getLines().clone();

			List<String> lines = meta.getLore();
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				line = line.substring(line.indexOf(' ') + 1);
				sign.setLine(i, p.hasPermission("signs.signcolors") ? ChatColor.translateAlternateColorCodes('&', line) : line);
			}

			sign.update();

			if (!Arrays.equals(originLines, sign.getLines()) && Config.PASTE_COSTS && !canBypass(p, "pastecosts")) {
				p.getInventory().removeItem(Config.INK.toItemStack());
			}
			break;
		default:
			break;
		}
		p.updateInventory();
	}

	private boolean canBypass(Player p, String pass) {
		return p.getGameMode() == GameMode.CREATIVE || p.hasPermission("signs.bypass." + pass) || p.hasPermission("signs.bypass.*");
	}
}
