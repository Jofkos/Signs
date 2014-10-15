package com.jofkos.signs.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@SuppressWarnings({"deprecation"})
public class MaterialType {
		
	private Material mat;
	private byte data;
	
	public MaterialType(Material mat) {
		this(mat, 0);
	}
	
	public MaterialType(Material mat, int data) {
		this.mat = mat;
		this.data = (byte) data;
	}
	
	public String toString() {
		return data == 0 ? this.mat.toString() : this.mat.toString() + ":" + this.data;
	}
	
	public ItemStack toItemStack() {
		return new ItemStack(this.mat, 1, data);
	}
	
	public MaterialData toMaterialData() {
		return new MaterialData(this.mat, this.data);
	}
	
	public Material getMaterial() {
		return this.mat;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == null ? false : obj instanceof MaterialType ? equals((MaterialType) obj) : obj instanceof ItemStack ? equals((ItemStack) obj) : false;
	}
	
	private boolean equals(MaterialType obj) {
		return this.mat == obj.mat && this.data == obj.data;
	}
	
	private boolean equals(ItemStack item) {
		return item == null ? false : item.getType() == this.mat && item.getData().getData() == data;
	}
	
	public static MaterialType fromString(String str) {
		String[] args = str.trim().split(":");
		
		Material material;
		try {
			material = Material.getMaterial(Integer.parseInt(args[0]));
		} catch (NumberFormatException e) {
			material = Material.getMaterial(args[0].toUpperCase());
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		if (material == null) {
			return null;
		}
		if (args.length > 1) {
			try {
				return new MaterialType(material, Integer.parseInt(args[1]));
			} catch (NumberFormatException e) {}
		}
		return new MaterialType(material);
	}
}
