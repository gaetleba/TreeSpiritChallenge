package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSpawnListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFirstJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		GreatTree tree = GreatTree.getGreatTree(player);

		player.setAllowFlight(true);

		if (tree == null && !GreatTree.isNew(player))
		{
			GreatTree.addNewPlayer(player);
			player.getInventory().addItem(
					new ItemStack(Material.LOG, 1, (short) 0, (byte) 0));
			player.getInventory().addItem(
					new ItemStack(Material.LOG, 1, (short) 0, (byte) 1));
			player.getInventory().addItem(
					new ItemStack(Material.LOG, 1, (short) 0, (byte) 2));
			player.getInventory().addItem(
					new ItemStack(Material.LOG, 1, (short) 0, (byte) 3));
		}
	}
}