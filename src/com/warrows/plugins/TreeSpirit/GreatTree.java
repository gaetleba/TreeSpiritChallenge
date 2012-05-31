package com.warrows.plugins.TreeSpirit;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GreatTree
{
	private Player						player;
	private Block						heart;
	private HashMap<Location, Block>	body;
	private HashSet<ItemStack>			drops;
	private int							score;

	public GreatTree(Block heart, Player player)
	{
		this.heart = heart;
		this.player = player;
		this.body = new HashMap<Location, Block>();
		this.drops = new HashSet<ItemStack>();
		this.score = 1;
		
		body.put(heart.getLocation(), heart);
	}

	private double getDistMax()
	{
		return 2 + Math.log(score);
	}

	public boolean isInBody(Block block)
	{
		return body.containsValue(block);
	}

	public boolean hasDrop(ItemStack item)
	{
		return drops.contains(item);
	}

	public void takeDrop(ItemStack item)
	{
		drops.remove(item);
	}

	public void addDrop(ItemStack item)
	{
		drops.add(item);
	}

	public boolean isAdjacent(Block block)
	{
		BlockFace[] directions =
		{ BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST,
				BlockFace.UP, BlockFace.DOWN };
		for (BlockFace direction : directions)
			if (isInBody(block.getRelative(direction)))
				return true;
		/*
		 * A reactiver si on veux que le joueur puisse placer ses blocs plus bas
		 * sans en placer en dessous. block = block.getRelative(BlockFace.DOWN);
		 * BlockFace[] directionsUp = { BlockFace.NORTH, BlockFace.SOUTH,
		 * BlockFace.WEST, BlockFace.EAST }; for (BlockFace direction :
		 * directionsUp) if (isInBody(block.getRelative(direction))) return
		 * true;
		 */
		return false;
	}

	public boolean isAtProximity(Block block)
	{
		for (double x = -getDistMax(); x < getDistMax(); x++)
			for (double y = -getDistMax(); y < getDistMax(); y++)
				for (double z = -getDistMax(); z < getDistMax(); z++)
				{
					Block b = block.getLocation()
							.add(new Location(TreeSpiritPlugin.world, x, y, z))
							.getBlock();
					if (isInBody(b))
						return true;
				}
		return false;
	}

	public void addToBody(Block block)
	{
		body.put(block.getLocation(), block);
		TreeSpiritPlugin.addTreeBlock(block,this);
	}

	public double getDistanceAllowed()
	{
		return 2;
	}
}
