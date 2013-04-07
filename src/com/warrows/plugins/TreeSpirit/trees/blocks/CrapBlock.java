package com.warrows.plugins.TreeSpirit.trees.blocks;

import org.bukkit.Location;

public class CrapBlock extends TreeBlock
{
	private static final long serialVersionUID = -5242640187336269964L;

	public CrapBlock(Location loc)
	{
		super(loc);
	}

	public boolean canBePartOfATree()
	{
		return false;
	}
}
