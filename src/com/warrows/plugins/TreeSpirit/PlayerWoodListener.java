package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerWoodListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupEvent(PlayerPickupItemEvent event)
	{
		ItemStack item = event.getItem().getItemStack();
		if (item.getType() != Material.LOG
				&& event.getItem().getItemStack().getType() != Material.SAPLING)
		{
			return;
		}

		Player player = event.getPlayer();
		GreatTree tree = TreeSpiritPlugin.getGreatTree(player);

		if (!tree.hasDrop(item))
		{
			event.getItem().remove();
			event.setCancelled(true);
		} else
		{
			tree.takeDrop(item);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropEvent(PlayerDropItemEvent event)
	{
		ItemStack item = event.getItemDrop().getItemStack();
		if (item.getType() != Material.LOG
				&& item.getType() != Material.SAPLING)
		{
			return;
		}

		Player player = event.getPlayer();
		GreatTree tree = TreeSpiritPlugin.getGreatTree(player);

		tree.addDrop(item);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBlockPlace(BlockPlaceEvent event)
	{
		Block item = event.getBlock();
		if (item.getType() != Material.LOG
				&& item.getType() != Material.SAPLING)
		{
			return;
		}

		Player player = event.getPlayer();
		GreatTree tree = TreeSpiritPlugin.getGreatTree(player);

		if (tree.isAdjacent(item))
			tree.addToBody(item);
		else
			event.setCancelled(true);
	}
}
