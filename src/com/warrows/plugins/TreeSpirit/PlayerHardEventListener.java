package com.warrows.plugins.TreeSpirit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerHardEventListener implements Listener
{
	@EventHandler(ignoreCancelled = true)
	public void onPlayerConnect(PlayerJoinEvent event)
	{
		TreeSpiritPlugin.log.info("test1");
		Player player = event.getPlayer();
		if (GreatTree.getGreatTree(player) != null)
			player.setAllowFlight(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		TreeSpiritPlugin.log.info("test2");
		Player player = event.getPlayer();
		GreatTree tree = GreatTree.getGreatTree(player);
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
}
