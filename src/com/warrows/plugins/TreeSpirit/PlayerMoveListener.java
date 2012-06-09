package com.warrows.plugins.TreeSpirit;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener
{
	private static HashMap<Player, Integer>	stuck	= new HashMap<Player, Integer>();

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		GreatTree tree = GreatTree.getGreatTree(player);
		if (tree == null)
			return;

		if (tree.isAtProximity(event.getTo().getBlock()))
		{
			stuck.remove(player);
		} else
		{
			if (stuck.get(player) == null)
				stuck.put(player, 0);
			else
			{
				int nbStuck = 1 + (Integer) stuck.get(player);
				if (nbStuck > 5)
				{
					Block destination = tree.getHeart();
					while (destination.getType() != Material.AIR)
						destination = destination.getRelative(BlockFace.UP);
					player.teleport(destination.getLocation());
					return;
				}
				stuck.put(player, nbStuck);

			}
			event.setCancelled(true);
			player.teleport(event.getFrom());
		}
	}
}