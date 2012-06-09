package com.warrows.plugins.TreeSpirit;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class TreeLife implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onLeavesDecayEvent(LeavesDecayEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(block);
		if (tree == null)
			return;
		tree.removeFromBody(block);
		tree.addDrops(block.getDrops());
	}

	@EventHandler(ignoreCancelled = true)
	public void onTreeGrowing(StructureGrowEvent event)
	{
		GreatTree tree = GreatTree.getGreatTree(event.getLocation().getBlock());
		if (tree == null)
			return;
		for (BlockState b : event.getBlocks())
			tree.addToBody(b.getBlock());
	}

	@EventHandler(ignoreCancelled = true)
	public void onTreeIgniting(BlockIgniteEvent event)
	{
		GreatTree tree = GreatTree.getGreatTree(event.getBlock());
		if (tree != null)
		{
			tree.removeFromBody(event.getBlock());
			return;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onTreeExplosion(EntityExplodeEvent event)
	{
		for (Block b : event.blockList())
		{
			GreatTree tree = GreatTree.getGreatTree(b);
			if (tree != null)
			{
				tree.removeFromBody(b);
				return;
			}
		}
	}
}