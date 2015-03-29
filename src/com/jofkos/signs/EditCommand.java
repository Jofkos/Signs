package com.jofkos.signs;

import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import com.jofkos.signs.utils.i18n.I18n;

public class EditCommand implements CommandExecutor {
	
	public static void load() {
		Signs.getInstance().getCommand("edit").setExecutor(new EditCommand());
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(I18n._("cmd.playeronly"));
			return true;
		}
		
		if (args.length < 2) {
			return false;
		}
		int line;
		try {
			line = Integer.valueOf(args[0]);
			if (line > 4 || line < 1)
				return false;
		} catch (Exception e) {
			return false;
		}
		
		Player player = (Player) cs;
		
		Block block = player.getTargetBlock((Set<Material>) null, 100);
		if (!(block.getState() instanceof Sign)) {
			player.sendMessage(I18n._("cmd.edit.signonly"));
			return true;
		}
		
		Sign sign = (Sign) block.getState();
		
		SignChangeEvent event = new SignChangeEvent(block, player, sign.getLines());
		event.setLine(line-1, StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " "));
		
		Bukkit.getPluginManager().callEvent(event);
		
		for (int i = 0; i < 4; i++) {
			sign.setLine(i, event.getLine(i));
		}
		sign.update();
		
		
		player.sendMessage(I18n._("cmd.edit.success", line, event.getLine(line-1)));
		
		return true;
	}

}
