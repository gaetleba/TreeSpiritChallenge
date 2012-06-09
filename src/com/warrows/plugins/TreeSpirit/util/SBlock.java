package com.warrows.plugins.TreeSpirit.util;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.warrows.plugins.TreeSpirit.TreeSpiritPlugin;

public class SBlock implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1017338353473611647L;
	private String worldName;
	private double x;
	private double y;
	private double z;
	
	public SBlock(Block b)
	{
		worldName = b.getWorld().getName();
		x = b.getLocation().getBlockX();
		y = b.getLocation().getBlockY();
		z = b.getLocation().getBlockZ();
	}
	
	public Block getBukkitBlock()
	{
		World world = TreeSpiritPlugin.getServerInstance().getWorld(worldName);
		return world.getBlockAt(new Location(world,x,y,z));
	}
	
	public boolean equals(Object o)
	{
		if (! (o instanceof SBlock))
			return false;
		SBlock sb = (SBlock) o;
		return worldName.equals(sb.worldName) && x==sb.x && y==sb.y && z==sb.z;
	}
	
	public String toString()
	{
		return (worldName+": X="+x+", Y="+y+", Z="+z);
	}
}
