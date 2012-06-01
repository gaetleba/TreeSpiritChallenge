package com.warrows.plugins.TreeSpirit;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

public class TreeLife implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onLeavesDecayEvent(LeavesDecayEvent event)
	{
		Block block = event.getBlock();
		GreatTree tree = TreeSpiritPlugin.getGreatTree(block);
		if (tree == null)
			return;
		for (ItemStack item : block.getDrops())
			tree.addDrop(item);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onTreeGrowing(StructureGrowEvent event)
	{
		GreatTree tree = TreeSpiritPlugin.getGreatTree(event.getLocation().getBlock());
		if (tree == null)
			return ;
		for (BlockState b : event.getBlocks())
		tree.addToBody(b.getBlock());
	}
}
