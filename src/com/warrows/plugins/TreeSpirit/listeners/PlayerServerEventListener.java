package com.warrows.plugins.TreeSpirit.listeners;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.warrows.plugins.TreeSpirit.TreeSpiritPlugin;
import com.warrows.plugins.TreeSpirit.trees.GreatTree;
import com.warrows.plugins.TreeSpirit.trees.TreesData;

public class PlayerServerEventListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerConnect(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (!TreesData.hasATree(player))
		{
			TreesData.loadGreatTree(player.getName());
		} 
		if (TreesData.hasATree(player))
		{
			player.setAllowFlight(true);
		}
		if (TreesData.isNew(player))
		{
			player.setGameMode(GameMode.CREATIVE);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		try
		{
			TreesData.saveGreatTree(player);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		GreatTree tree = TreesData.getGreatTree(player);
		if (tree != null)
		{
			player.setAllowFlight(true);
			Block destination = tree.getHeart();
			while (destination.getType() != Material.AIR
					|| destination.getRelative(BlockFace.UP).getType() != Material.AIR)
				destination = destination.getRelative(BlockFace.UP);
			player.teleport(destination.getLocation());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onReload(PluginEnableEvent event)
	{
		Plugin plugin = event.getPlugin();
		if ("TreeSpirit".equals(plugin.getName()))
			for (Player player : TreeSpiritPlugin.getServerInstance()
					.getOnlinePlayers())
			{
				TreesData.loadGreatTree(player.getName());
				if (TreesData.getGreatTree(player) != null)
					player.setAllowFlight(true);
			}

	}
}
