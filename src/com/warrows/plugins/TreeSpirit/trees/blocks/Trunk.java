package com.warrows.plugins.TreeSpirit.trees.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.warrows.plugins.TreeSpirit.trees.World;

public class Trunk extends TreeBlock
{
	private static final long serialVersionUID = -5242640187336269964L;

	private Trunk(Location loc)
	{
		super(loc);
	}

	public static Trunk newTrunk(Location loc, Block blockAgainst)
	{
		TreeBlock bro = World.getInstance().getTreeBlockAt(
				blockAgainst.getLocation());
		if (null == bro || !(bro instanceof Heart) && !(bro instanceof Trunk))
		{
			return null;
		} else
		{
			return new Trunk(loc);
		}
	}
}
