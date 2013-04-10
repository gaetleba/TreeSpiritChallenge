package com.warrows.plugins.TreeSpirit.trees.blocks;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.warrows.plugins.TreeSpirit.trees.World;

public class Trunk extends TreeBlock implements Popable, Solid
{
	private static final long	serialVersionUID	= -5242640187336269964L;

	private Solid				parent;
	private HashSet<Popable>	children;

	private Trunk(Location loc, Solid parent)
	{
		super(loc);
		children = new HashSet<Popable>();
		this.parent = parent;
		parent.addChild((Popable) this);
		BlockFace[] directions = { BlockFace.NORTH, BlockFace.SOUTH,
				BlockFace.WEST, BlockFace.EAST, BlockFace.UP, BlockFace.DOWN };
		for (BlockFace direction : directions)
		{
			TreeBlock neighbour = World.getInstance().getTreeBlockAt(
					location.getBlock().getRelative(direction).getLocation());
			if (neighbour instanceof Solid)
			{
				((Solid) neighbour).addChild(this);
				parent = (Solid) neighbour;
			}
			if (neighbour instanceof Popable)
			{
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
			return new Trunk(loc, (Solid) parent);
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
		if (parent == this.parent)
		{
			BlockFace[] directions = { BlockFace.NORTH, BlockFace.SOUTH,
					BlockFace.WEST, BlockFace.EAST, BlockFace.UP,
					BlockFace.DOWN };
			for (BlockFace direction : directions)
			{
				TreeBlock potentialParent = World.getInstance().getTreeBlockAt(
						location.getBlock().getRelative(direction)
								.getLocation());
				if (potentialParent != parent
						&& potentialParent instanceof Solid)
				{
					parent = (Solid) potentialParent;
				}
			}
		}
		if (parent == this.parent)
		{
			this.pop();
		}
	}

	@Override
	public void setParent(Solid parent)
	{
		this.parent = parent;
	}
}
