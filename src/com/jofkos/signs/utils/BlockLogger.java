package com.jofkos.signs.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.jofkos.signs.Signs;

public class BlockLogger implements Runnable {
	private List<String> msgs = new ArrayList<String>();
	
	public BlockLogger add(String... messages) {
		for (String message : messages) {
			for (String line : message.split("\n")) {
				msgs.add(line);
			}
		}
		return this;
	}
	
	public void out() {
		Bukkit.getScheduler().runTask(Signs.getInstance(), this);
	}
	
	@Override
	public void run() {
		Signs.log(this.msgs.toArray(new String[this.msgs.size()]));
		this.msgs.clear();
	}
}
