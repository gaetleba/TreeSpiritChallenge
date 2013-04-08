package com.warrows.plugins.TreeSpirit.trees;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.warrows.plugins.TreeSpirit.trees.blocks.*;

public class World implements Serializable
{
	private static final long serialVersionUID = 2984500717208722984L;
	private static World instance = null;

	private HashMap<Location, TreeBlock> blocksMap;
	private HashMap<Player, Tree> treesMap;

	private World()
	{
		blocksMap = new HashMap<Location, TreeBlock>();
		treesMap = new HashMap<Player, Tree>();
	}

	public static World getInstance()
	{
		if (instance == null)
		{
			instance = new World();
		}
		return instance;
	}

	public TreeBlock getTreeBlockAt(Location location)
	{
		TreeBlock block = blocksMap.get(location);
		if (null == block)
		{
			block = new CrapBlock(location);
		}
		return block;
	}

	public Tree getTreeOf(Player p)
	{
		return treesMap.get(p);
	}

	public Tree createTree(Player player, Location loc)
	{
		Tree tree = new Tree(player, loc);
		treesMap.put(player, tree);
		return tree;
	}

	public void addBlock(TreeBlock block)
	{
		blocksMap.put(block.getLocation(), block);
	}

	public void removeBlockAt(Location location)
	{
		blocksMap.remove(location);
	}
}
