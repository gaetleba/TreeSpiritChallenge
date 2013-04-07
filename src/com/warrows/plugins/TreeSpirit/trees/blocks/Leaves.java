package com.warrows.plugins.TreeSpirit.trees.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.warrows.plugins.TreeSpirit.trees.World;

public class Leaves extends TreeBlock
{
	private static final long serialVersionUID = -5242640187336269964L;

	private Leaves(Location loc)
	{
		super(loc);
	}

	public static Leaves newLeaves(Location loc, Block blockAgainst)
	{
		TreeBlock bro = World.getInstance().getTreeBlockAt(
				blockAgainst.getLocation());
		if (null == bro || !(bro instanceof Heart) && !(bro instanceof Leaves) && !(bro instanceof Trunk))
		{
			return null;
		} else
		{
			return new Leaves(loc);
		}
	}
}
