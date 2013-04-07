package com.warrows.plugins.TreeSpirit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class StartListener implements Listener
{
	@EventHandler(ignoreCancelled = false)
	public void playerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		player.setAllowFlight(true);
		if (hasPlayedBefore(player))
			return;
		start(player);
	}
	
	@EventHandler(ignoreCancelled = false)
	public void playerSpawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		player.setAllowFlight(true);
		start(player);
	}

	private void start(Player player)
	{		
		player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 0));
				player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 1));
				player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 2));
				player.getInventory().addItem(
				new ItemStack(Material.LOG, 1, (short) 3));
	}

	private boolean hasPlayedBefore(Player p)
	{
		return Bukkit.getOfflinePlayer(p.getName()).hasPlayedBefore();
	}
}
