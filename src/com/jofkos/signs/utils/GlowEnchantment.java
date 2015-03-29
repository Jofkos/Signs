package com.jofkos.signs.utils;
	
import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import com.jofkos.signs.utils.reflect.Reflect;
	
public class GlowEnchantment extends EnchantmentWrapper {
	
	private static Enchantment glow;
	private static int id = 250;
	private static String name = "Glow";
	
	public GlowEnchantment(int id) {
		super(id);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}
	
	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}
	
	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}
	
	@Override
	public int getMaxLevel() {
		return 10;
	}
	
	@Override
	public String getName() {
		return "Glow";
	}
	
	@Override
	public int getStartLevel() {
		return 1;
	}
	
	public static void load() {
		try {
			Reflect.set(Enchantment.class, "acceptingNew", true);
			
			HashMap<Integer, Enchantment> byId = Reflect.get(Enchantment.class, "byId");
			HashMap<String, Enchantment> byName = Reflect.get(Enchantment.class, "byName");
			
			if (byId.containsKey(id)) {
				byId.remove(id);
			}
			
			if (byName.containsKey(name)) {
				byName.remove(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		glow = new GlowEnchantment(id);
		Enchantment.registerEnchantment(glow);
	}
	
	public static Enchantment getGlow() {
		if (glow == null) {
			load();
		}
		
		return glow;
	}
	
}