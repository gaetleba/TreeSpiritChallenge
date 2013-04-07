package com.warrows.plugins.TreeSpirit.trees;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import com.warrows.plugins.TreeSpirit.trees.blocks.CrapBlock;
import com.warrows.plugins.TreeSpirit.trees.blocks.Heart;
import com.warrows.plugins.TreeSpirit.trees.blocks.TreeBlock;

public class Tree
{
	private byte type;
	private Heart heart;

	public Tree(Player player, Location loc)
	{
		type = loc.getBlock().getData();
		heart = new Heart(loc);
		World.getInstance().addBlock(heart);
		startInventory(player);
	}

	private void startInventory(Player player)
	{
		Inventory inv = player.getInventory();
		inv.clear();
		inv.addItem(new ItemStack(Material.LOG, 5, (short) type));
		inv.addItem(new ItemStack(Material.SAPLING, 3, (short) type));

		Dye d = new Dye();
		d.setColor(DyeColor.WHITE);
		inv.addItem(d.toItemStack(5));
	}

	public boolean isAtProximity(Block block)
	{
		for (double x = -getDistMax() + 1; x < getDistMax(); x += 1)
			for (double y = -getDistMax() + 1; y < getDistMax() + 1; y += 1)
				for (double z = -getDistMax() + 1; z < getDistMax(); z += 1)
				{
					TreeBlock b = World.getInstance().getTreeBlockAt(
							block.getLocation().add(
									new Location(block.getWorld(), x, y, z)));
					if (!(b instanceof CrapBlock))
						return true;
				}
		return false;
	}

	private int getDistMax()
	{
		return 2;
	}

	public TreeBlock getHeart()
	{
		return heart;
	}

}
