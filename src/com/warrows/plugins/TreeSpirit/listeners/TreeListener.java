package com.warrows.plugins.TreeSpirit.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

import com.warrows.plugins.TreeSpirit.trees.GreatTree;
import com.warrows.plugins.TreeSpirit.trees.TreesData;

public class TreeListener implements Listener
{
	/* Suivent les évènements suceptibles d'aggrandir un arbre */

	@EventHandler(ignoreCancelled = true)
	public void onTreeGrowing(StructureGrowEvent event)
	{
		GreatTree tree = TreesData.getGreatTree(event.getLocation().getBlock());
		if (tree == null)
			return;
		for (BlockState b : event.getBlocks())
			if (b.getType() == Material.LOG
					|| b.getType() == Material.SAPLING
					|| b.getType() == Material.LEAVES)
				tree.addToBody(b.getBlock());
	}

	/* Suivent les évènements suceptibles de reduire un arbre */

	@EventHandler(ignoreCancelled = true)
	public void onBlockBurnEvent(BlockBurnEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = TreesData.getGreatTree(block);
		if (tree != null)
			TreesData.destroyBlock(tree, block, event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockFadeEvent(BlockFadeEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = TreesData.getGreatTree(block);
		if (tree != null)
			TreesData.destroyBlock(tree, block, event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onLeavesDecayEvent(LeavesDecayEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = TreesData.getGreatTree(block);
		if (tree != null)
			TreesData.destroyBlock(tree, block, event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onTreeExplosion(EntityExplodeEvent event)
	{
		for (Block b : event.blockList())
		{
			Block block = b;
			GreatTree tree = TreesData.getGreatTree(block);
			if (tree != null)
				TreesData.destroyBlock(tree, b, event);
		}
	}
}