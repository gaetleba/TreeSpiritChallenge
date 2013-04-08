package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Woody
{
	private Material material;

	public Woody(Block block)
	{
		material = block.getType();
	}
	
	public boolean isWoody()
	{
		if (material == Material.LOG)
			return true;
		if (material == Material.LEAVES)
			return true;
		if (material == Material.SAPLING)
			return true;
		return false;
	}
}
