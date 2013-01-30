package com.warrows.plugins.TreeSpirit.trees.components;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.warrows.plugins.TreeSpirit.TreeSpiritPlugin;

public class TreeBlock implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1017338353473611647L;
	private String worldName;
	private double x;
	private double y;
	private double z;
	private TreeBlock neigh[];
	
	public TreeBlock(Block b)
	{
		worldName = b.getWorld().getName();
		x = b.getLocation().getBlockX();
		y = b.getLocation().getBlockY();
		z = b.getLocation().getBlockZ();
		neigh = new TreeBlock[6];
	}
	
	public TreeBlock(String s)
	{
		worldName = s.substring(s.indexOf("worldName: ")+11,s.indexOf("X=")-1);
		x = (int) Double.parseDouble(s.substring(s.indexOf(" X=")+3,s.indexOf(" Y=")));
		y = (int) Double.parseDouble(s.substring(s.indexOf(" Y=")+3,s.indexOf(" Z=")));
		z = (int) Double.parseDouble(s.substring(s.indexOf(" Z=")+3,s.indexOf("]")));
	}
	
	public Block getBukkitBlock()
	{
		World world = TreeSpiritPlugin.getServerInstance().getWorld(worldName);
		return world.getBlockAt(new Location(world,x,y,z));
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (! (o instanceof TreeBlock))
			return false;
		TreeBlock sb = (TreeBlock) o;
		return worldName.equals(sb.worldName) && x==sb.x && y==sb.y && z==sb.z;
	}
	
	@Override
	public int hashCode()
	{
		return (worldName.hashCode()+10*(int)x+100*(int)y+1000*(int)z);
	}
	
	public String toString()
	{
		return ("Block [worldName: "+worldName+" X="+x+" Y="+y+" Z="+z+"]");
	}
}
