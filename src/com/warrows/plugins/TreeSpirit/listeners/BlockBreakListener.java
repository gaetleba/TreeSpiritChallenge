package com.warrows.plugins.TreeSpirit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.warrows.plugins.TreeSpirit.Woody;
import com.warrows.plugins.TreeSpirit.trees.World;
import com.warrows.plugins.TreeSpirit.trees.blocks.Popable;
import com.warrows.plugins.TreeSpirit.trees.blocks.TreeBlock;

public class BlockBreakListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void BlockBreak(BlockBreakEvent event)
	{
		World world = World.getInstance();
		if (!new Woody(event.getBlock()).isWoody())
			return;
		TreeBlock block = world.getTreeBlockAt(event.getBlock().getLocation());
		if (null == block || !(block instanceof Popable))
		{
			System.out.println(block);
			System.out.println(block.getClass());
			return;
		}
		event.setCancelled(true);
		((Popable) block).pop();
	}
}
