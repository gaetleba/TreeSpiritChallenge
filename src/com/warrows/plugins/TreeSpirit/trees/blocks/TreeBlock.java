package com.warrows.plugins.TreeSpirit.trees.blocks;

import java.io.Serializable;

import org.bukkit.Location;

public abstract class TreeBlock implements Serializable
{
	private static final long	serialVersionUID	= -1017338353473611647L;
	
	private Location location;
	
	protected TreeBlock(Location loc)
	{
		location = loc;
	}

	public boolean canBePartOfATree()
	{
		return true;
	}
	
	public Location getLocation()
	{
		return location;
	}
}
