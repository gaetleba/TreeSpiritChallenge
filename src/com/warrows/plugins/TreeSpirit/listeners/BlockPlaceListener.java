package com.warrows.plugins.TreeSpirit.listeners;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.warrows.plugins.TreeSpirit.Woody;
import com.warrows.plugins.TreeSpirit.trees.Tree;
import com.warrows.plugins.TreeSpirit.trees.World;
import com.warrows.plugins.TreeSpirit.trees.blocks.CrapBlock;
import com.warrows.plugins.TreeSpirit.trees.blocks.Heart;
import com.warrows.plugins.TreeSpirit.trees.blocks.Leaves;
import com.warrows.plugins.TreeSpirit.trees.blocks.Sapling;
import com.warrows.plugins.TreeSpirit.trees.blocks.TreeBlock;
import com.warrows.plugins.TreeSpirit.trees.blocks.Trunk;

public class BlockPlaceListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void BlockPlace(BlockPlaceEvent event)
	{
		World world = World.getInstance();
		if (!new Woody(event.getBlockPlaced()).isWoody())
			return;
		
		Player player = event.getPlayer();
		Tree tree = world.getTreeOf(player);
		Location loc = event.getBlock().getLocation();
		if (null == tree)
		{
			tree = world.createTree(player, loc);
			return;
		}

		TreeBlock block = createTreeBlock(event.getBlock(), event.getBlockAgainst());
		if (null == block)
			event.setCancelled(true);
		else
			world.addBlock(block);
	}
	
	private TreeBlock createTreeBlock(Block block, Block blockAgainst)
	{
		TreeBlock treeBlock;
		switch (block.getType())
		{
		case LOG:
			treeBlock = Trunk.newTrunk(block.getLocation(),blockAgainst);
			break;
		case LEAVES:
			treeBlock = Leaves.newLeaves(block.getLocation(),blockAgainst);
			break;
		case SAPLING:
			treeBlock = Sapling.newSapling(block.getLocation(),blockAgainst);
			break;
		case GLOWSTONE:
			treeBlock = new Heart(block.getLocation());
			break;
		default:
			treeBlock = new CrapBlock(block.getLocation());
			return treeBlock;
		}
		return treeBlock;
	}
}
