package com.warrows.plugins.TreeSpirit;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class TreeLife implements Listener
{
	/* Suivent les évènements suceptibles d'aggrandir un arbre*/

	@EventHandler(ignoreCancelled = true)
	public void onTreeGrowing(StructureGrowEvent event)
	{
		GreatTree tree = GreatTree.getGreatTree(event.getLocation().getBlock());
		if (tree == null)
			return;
		for (BlockState b : event.getBlocks())
			tree.addToBody(b.getBlock());
	}
	

	/* Suivent les évènements suceptibles de reduire un arbre*/
	

	@EventHandler(ignoreCancelled = true)
	public void onBlockBurnEvent(BlockBurnEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(block);
		if (tree != null)
			PlayerWoodListener.destroyBlock(tree, block, event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockFadeEvent(BlockFadeEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(block);
		if (tree != null)
			PlayerWoodListener.destroyBlock(tree, block, event);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onLeavesDecayEvent(LeavesDecayEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = GreatTree.getGreatTree(block);
		if (tree != null)
			PlayerWoodListener.destroyBlock(tree, block, event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onTreeExplosion(EntityExplodeEvent event)
	{
		for (Block b : event.blockList())
		{
			Block block = b;
			GreatTree tree = GreatTree.getGreatTree(block);
			if (tree != null)
				PlayerWoodListener.destroyBlock(tree, b, event);
		}
	}
}