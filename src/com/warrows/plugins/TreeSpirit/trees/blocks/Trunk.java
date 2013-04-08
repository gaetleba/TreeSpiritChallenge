package com.warrows.plugins.TreeSpirit.trees.blocks;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.warrows.plugins.TreeSpirit.trees.World;

public class Trunk extends TreeBlock implements Popable, Solid
{
	private static final long serialVersionUID = -5242640187336269964L;

	private HashSet<Solid> parents;
	private HashSet<Popable> children;

	private Trunk(Location loc, Solid parent)
	{
		super(loc);
		parents = new HashSet<Solid>();
		children = new HashSet<Popable>();
		parents.add(parent);
		parent.addChild((Popable)this);
		BlockFace[] directions =
			{ BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST,
			BlockFace.UP, BlockFace.DOWN };
			for (BlockFace direction : directions)
			{
				TreeBlock neighbour = World.getInstance().getTreeBlockAt(location.getBlock().getRelative(direction).getLocation());
				if (neighbour instanceof Solid)
				{
					((Solid) neighbour).addChild(this);
					this.addParent((Solid) neighbour);
				}
				if (neighbour instanceof Popable)
				{
					((Popable) neighbour).addParent(this);
					this.addChild((Popable) neighbour);
				}
			}
	}
	
	public void addChild(Popable child)
	{
		children.add(child);
	}

	public static Trunk newTrunk(Location loc, Block blockAgainst)
	{
		TreeBlock parent = World.getInstance().getTreeBlockAt(
				blockAgainst.getLocation());
		if (null == parent || !(parent instanceof Solid))
		{
			return null;
		} else
		{
			return new Trunk(loc, (Solid)parent);
		}
	}

	public void pop()
	{
		World world = World.getInstance();
		for (Popable child : children)
		{
			child.removeParent(this);
		}
		world.removeBlockAt(location);
		location.getBlock().breakNaturally();
	}

	@Override
	public void removeParent(Solid parent)
	{
		parents.remove(parent);
		if (parents.isEmpty())
		{
			this.pop();
		}
	}

	@Override
	public void addParent(Solid parent)
	{
		parents.add(parent);
	}
}
